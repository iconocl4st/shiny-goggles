#/bin/bash
zip imageconf.zip -r config/images/
zip test_images.zip -r screenshots/
zip python_data.zip -r python/data
zip all_data.zip imageconf.zip test_images.zip python_data.zip
rm python_data.zip test_images.zip imageconf.zip
