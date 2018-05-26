# This comes from https://github.com/narenkmanoharan/ImageNet-Classifier-Tensorflow/blob/master/alex_net.py

import tensorflow as tf
import tflearn as tfl
import json
import numpy as np
import os
import shutil

print("Loading data")
with open("data/training_set_x.json", "r") as inx:
    xdata = json.load(inx)
    image_width = xdata['image-width']
    image_height = xdata['image-height']
    X = np.array(xdata['array'])

print(X.shape)

with open("data/training_set_y.json", "r") as iny:
    ydata = json.load(iny)
    y_dim = ydata['y-dim']
    hero_dim = ydata["hero-dim"]
    state_dim = ydata["state-dim"]
    Y = np.array(ydata['array'])

if hero_dim + state_dim != y_dim:
    raise Exception("bad dimensions")

print(Y.shape)

with open("data/training_set_a.json", "r") as ina:
    adata = json.load(ina)
    additional_dim = adata['additional-dim']
    A = np.array(adata['array'])

print(A.shape)
print("Done loading data")


# Image Processing using zero center and standard normalization
img_pre_processing = tfl.data_preprocessing.ImagePreprocessing()
img_pre_processing.add_featurewise_zero_center()
img_pre_processing.add_featurewise_stdnorm()

image_input = tf.placeholder(shape=[None, image_width, image_height, 3], dtype=tf.float32, name='images_placeholder')
feature_input = tf.placeholder(shape=[None, additional_dim], dtype=tf.float32, name='feature_placeholder')

auto_picker = tfl.layers.core.input_data(placeholder=image_input, name='images')
additional_info = tfl.layers.core.input_data(placeholder=feature_input, name='precomputed')

auto_picker = tfl.layers.conv.conv_2d(auto_picker, 96, 11, strides=4, activation='relu')
auto_picker = tfl.layers.conv.max_pool_2d(auto_picker, 3, strides=2)
auto_picker = tfl.layers.normalization.local_response_normalization(auto_picker)
auto_picker = tfl.layers.conv.conv_2d(auto_picker, 256, 5, activation='relu')
auto_picker = tfl.layers.conv.max_pool_2d(auto_picker, 3, strides=2)
auto_picker = tfl.layers.normalization.local_response_normalization(auto_picker)
auto_picker = tfl.layers.conv.conv_2d(auto_picker, 384, 3, activation='relu')
auto_picker = tfl.layers.conv.conv_2d(auto_picker, 384, 3, activation='relu')
auto_picker = tfl.layers.conv.conv_2d(auto_picker, 256, 3, activation='relu')
auto_picker = tfl.layers.conv.max_pool_2d(auto_picker, 3, strides=2)
auto_picker = tfl.layers.normalization.local_response_normalization(auto_picker)
auto_picker = tfl.layers.flatten(auto_picker)

auto_picker = tfl.layers.merge_ops.merge(tensors_list=[auto_picker, additional_info], mode='concat', axis=1)
auto_picker = tfl.layers.core.fully_connected(auto_picker, 4096, activation='tanh')
auto_picker = tfl.layers.core.dropout(auto_picker, 0.5)
auto_picker = tfl.layers.core.fully_connected(auto_picker, 4096, activation='tanh')
auto_picker = tfl.layers.core.dropout(auto_picker, 0.5)

hero_picker = tfl.layers.core.fully_connected(auto_picker, 4096, activation='tanh')
hero_picker = tfl.layers.core.dropout(hero_picker, 0.5)
hero_picker = tfl.layers.core.fully_connected(hero_picker, hero_dim, activation='softmax')

stte_picker = tfl.layers.core.fully_connected(auto_picker, 1024, activation='tanh')
stte_picker = tfl.layers.core.dropout(stte_picker, 0.5)
stte_picker = tfl.layers.core.fully_connected(stte_picker, state_dim, activation='softmax')

auto_picker = tfl.layers.merge_ops.merge(tensors_list=[hero_picker, stte_picker], mode='concat', axis=1, name='classified')

regression = tfl.layers.estimator.regression(auto_picker, optimizer='momentum', loss='categorical_crossentropy', learning_rate=0.001, name="regression")

model = tfl.DNN(regression, checkpoint_path='checkpoints/checkpoint', max_checkpoints=1, tensorboard_verbose=2)
model.load('last_model/model.tfl')
model.fit({ image_input: X, feature_input: A }, Y, n_epoch=30, validation_set=0.1, shuffle=True, show_metric=True, batch_size=64, run_id='dota_autopicker')
model.save('last_model/model.tfl')

output = model.predictor.session.run(auto_picker, feed_dict={ image_input: X,  feature_input: A })
with open('results.json', 'w') as outjs:
    json.dump({ 'results': [[float(e) for e in o] for o in output] }, outjs)


output_directory = './saved_model/'
if os.stat(output_directory):
    shutil.rmtree(output_directory)


signature = tf.saved_model.signature_def_utils.build_signature_def(
    inputs = {
        'images': tf.saved_model.utils.build_tensor_info(image_input),
        'additional': tf.saved_model.utils.build_tensor_info(feature_input)
    },
    outputs = {'classification': tf.saved_model.utils.build_tensor_info(auto_picker)},
    method_name="classify"
)


saver = tf.saved_model.builder.SavedModelBuilder(output_directory)
saver.add_meta_graph_and_variables(
    model.predictor.session,
    [tf.saved_model.tag_constants.SERVING],
    signature_def_map={tf.saved_model.signature_constants.DEFAULT_SERVING_SIGNATURE_DEF_KEY: signature}
)
saver.save()
