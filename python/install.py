#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import time
#os.system('adb pull data/log/android_logs C:\Users\Default\Desktop\logs');
#os.system('adb pull product/hw_oem/ C:\Users\Default\Desktop\prop');
#os.system('adb shell');

#不区分大小写
dir = input('请输入你想编译的目录：').strip();

#列出apps目录下的所有文件
apks = os.listdir('apps');

#确认大小写
apkName = input('请输入你想编译的apk名称的前几个字母：').strip();


for apk in apks:
    if apk.endswith('.apk'):
        if apk.startswith(apkName):
            os.system('adb install -r -d ' + dir + '/' +apk);
        