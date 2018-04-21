#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os

path = input('请输入要build的prop文件路径：');
os.system('adb shell chmod 644 ' + path);

isY = input('是否reboot：').strip();
if 'y' == isY or 'Y' == isY:
    os.system('adb reboot');