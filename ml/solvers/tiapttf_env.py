from subprocess import Popen, PIPE


class TiapttfEnv:

    def __init__(self, width: int, height: int, combo_size: int):
        self._width = width
        self._height = height
        self._combo_size = combo_size
        self._instance = Popen(['java', '-jar', '../../cli_launcher.jar'], stdout=PIPE, stdin=PIPE)

    def reset(self):
        self._instance.kill()
        self._instance = Popen(['java', '-jar', '../../cli_launcher.jar'], stdout=PIPE, stdin=PIPE)
        return self._instance.stdout.readline()

    def step(self, action):
        x = action % self._width
        y = action / self._width
        self._instance.stdin.write(bytes("{},{}".format(x, y), "utf-8"))
        return self._instance.stdout.readline()
