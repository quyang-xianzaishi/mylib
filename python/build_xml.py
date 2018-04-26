#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import sys

version = sys.version

if sys.version.startswith('2'):
    path = raw_input("please input path:").strip()
else:
    path = input('please input path:').strip()

root_dir = path.split('/')[1]
os.system("adb shell mount '-o remount,rw "+'/'+ root_dir +"'")
os.system('adb shell chmod 644 ' + path);
os.system('adb reboot');




