# This comes from https://github.com/narenkmanoharan/ImageNet-Classifier-Tensorflow/blob/master/alex_net.py

import tensorflow as tf
import tflearn as tfl
import json
import numpy as np

# Importing the Flower datasets

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
    Y = np.array(ydata['array'])
print(Y.shape)

with open("data/training_set_a.json", "r") as ina:
    adata = json.load(ina)
    additional_dim = adata['additional-dim']
    A = np.array(adata['array'])
print("Done loading data")

# Image Processing using zero center and standard normalization
img_pre_processing = tfl.data_preprocessing.ImagePreprocessing()
img_pre_processing.add_featurewise_zero_center()
img_pre_processing.add_featurewise_stdnorm()

# Creating data model for the alexnet model
alex_net_max = tfl.layers.core.input_data(shape=[None, image_width, image_height, 3])

# 2D Convolution
alex_net_max = tfl.layers.conv.conv_2d(alex_net_max, 96, 11, strides=4, activation='relu')

# 2D Max-pooling
alex_net_max = tfl.layers.conv.max_pool_2d(alex_net_max, 3, strides=2)

# Normalization to decrease overfitting
alex_net_max = tfl.layers.normalization.local_response_normalization(alex_net_max)

# 2D Convolution
alex_net_max = tfl.layers.conv.conv_2d(alex_net_max, 256, 5, activation='relu')

# 2D Max-pooling
alex_net_max = tfl.layers.conv.max_pool_2d(alex_net_max, 3, strides=2)

# Normalization to decrease overfitting
alex_net_max = tfl.layers.normalization.local_response_normalization(alex_net_max)

# 2D Convolution
alex_net_max = tfl.layers.conv.conv_2d(alex_net_max, 384, 3, activation='relu')

# 2D Convolution
alex_net_max = tfl.layers.conv.conv_2d(alex_net_max, 384, 3, activation='relu')

# 2D Convolution
alex_net_max = tfl.layers.conv.conv_2d(alex_net_max, 256, 3, activation='relu')

# 2D Max-pooling
alex_net_max = tfl.layers.conv.max_pool_2d(alex_net_max, 3, strides=2)

# Normalization to decrease overfitting
alex_net_max = tfl.layers.normalization.local_response_normalization(alex_net_max)

# Fully connected layer
alex_net_max = tfl.layers.core.fully_connected(alex_net_max, 4096, activation='tanh')

# Dropout layer
alex_net_max = tfl.layers.core.dropout(alex_net_max, 0.5)

# Fully connected layer
alex_net_max = tfl.layers.core.fully_connected(alex_net_max, 4096, activation='tanh')

# Dropout layer
alex_net_max = tfl.layers.core.dropout(alex_net_max, 0.5)

# Fully connected layer
alex_net_max = tfl.layers.core.fully_connected(alex_net_max, y_dim, activation='softmax')

# Constructing an estimator using regression
alex_net_max = tfl.layers.estimator.regression(alex_net_max, optimizer='momentum', loss='categorical_crossentropy', learning_rate=0.001)

# Constructing the Deep Neural Network using created model
model = tfl.DNN(alex_net_max, checkpoint_path='alexnet_max', max_checkpoints=1, tensorboard_verbose=2)

# Fitting the model with the validation set
model.fit(X, Y, n_epoch=1000, validation_set=0.1, shuffle=True, show_metric=True, batch_size=64, run_id='alex_flower_max')


# This code does not work
#with open("autopicker_model.json", "w") as model_save_file:
#    model_json = model.to_json()
#    model_save_file.write(model_json)

# save model weights
#model.save_weights("autopicker_weights.data")
