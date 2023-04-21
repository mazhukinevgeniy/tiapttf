import keras
from keras import layers


def create_q_model(width: int, height: int):
    """
    field encoded, (selection.x, selection.y), (multiplier)
    """
    inputs = layers.Input(
        shape=(width * height, 2, 1)
    )

    # Convolutions
    layer1 = layers.Conv2D(width * height / 8, 8, strides=2, activation="relu")(inputs)
    layer2 = layers.Conv2D(width * height / 4, 4, strides=2, activation="relu")(layer1)
    layer3 = layers.Conv2D(width * height / 4, 3, strides=1, activation="relu")(layer2)

    layer4 = layers.Flatten()(layer3)

    layer5 = layers.Dense(512, activation="relu")(layer4)
    action = layers.Dense(width * height, activation="linear")(layer5)

    return keras.Model(inputs=inputs, outputs=action)
