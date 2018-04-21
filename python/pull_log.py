#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import sys

#必须加r
path = r'C:\Users\Default\Desktop\logs';
is_exist = os.path.exists(path);

if not is_exist:
    os.makedirs(path);	

os.popen('adb remount');
os.popen('adb pull /data/log/android_logs '+ path);    
os.system('start explorer ' + path);
    
    
    
