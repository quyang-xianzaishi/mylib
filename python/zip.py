import tkinter
import tkinter.filedialog
import zipfile
import os
import tkinter.messagebox

root = tkinter.Tk()
root.minsize(340, 350)
root.title('主页面')


# 定义一个变量储存文件路径
filepath = []

#添加压缩文件
def add_file():
    # 全局化变量
    global filepath
    openfilepath = tkinter.filedialog.askopenfilenames()
    # 将所选的多个文件路径添加到列表中储存
    filepath += list(openfilepath)
    if filepath != []:
        # 使用换行符链接成字符串
        filestr = '\n'.join(filepath)
        # 将文件路径写入label
        label1['text'] = filestr
    else:
        label1['text'] = '显示压缩文件路径'

#压缩文件
def zip_file():
    #全局化变量
    global filepath
    if label1['text'] != '显示压缩文件路径'and label1['text'] != '':
        #选择压缩的地址并命名
        a = tkinter.filedialog.asksaveasfile()
        if a != None:
            #获取所选位置的完整路径
            path = a.name
            #分割路径的后缀部分和其他部分
            houzhui2 = os.path.splitext(path)
            #判断后缀是否是zip格式
            if houzhui2[1] == '.zip':
                # 打开或者创建压缩文件
                newzipfile = zipfile.ZipFile(path, 'w')
                # 添加压缩内容
                # 遍历所有路径，加入压缩文件
                for filename in filepath:
                    newzipfile.write(filename, os.path.basename(filename))  # 参数2 获取完整路径的整体部分
                # 关闭文件
                newzipfile.close()
                #清空储存列表（防止第二次压缩时，上次文件存在）
                filepath = []
                #重置label标签内容
                label1['text'] = '显示压缩文件路径'
                # 判断文件是否压缩成功
                if os.path.exists(path):
                    tkinter.messagebox.showinfo(title='提示', message='文件压缩成功' + path)
                else:
                    tkinter.messagebox.showerror(title='错误', message='文件压缩失败')
            else:
                #文件压缩必须是zip 格式，否则提示错误
                tkinter.messagebox.showerror(title='错误', message='文件压缩格式必须是zip')
        else:
            pass
    else:
        #如果没有添加压缩文件，就是提示错误
        tkinter.messagebox.showerror(title= '错误',message = '请添加压缩文件')

#解压文件
def unzipfile():
    flag = False
    # 选择需要解压的文件路径
    unzipname = tkinter.filedialog.askopenfilename(title = '请选择要解压的文件')
    #判断文件路径是否存在
    if os.path.exists(unzipname):
        #将文件路径分割为后缀部分和其他部分
        houzhui = os.path.splitext(unzipname)
        #判断获取的文件后缀是否是.zip
        if houzhui[1] == '.zip':#如果是就进行压缩
            # 选择解压到哪的路径
            unzipath = tkinter.filedialog.askdirectory(title = '请选择解压路径')
            # 开始解压
            zp = zipfile.ZipFile(unzipname)
            # 解压所有
            zp.extractall(unzipath)
            #遍历压缩包里所有文件名
            for i in zp.namelist():
                #判断路径和文件名是否存在
                if os.path.exists(unzipath + '/'+ i):
                    flag = True
                else:
                    flag = False
                    break
            if flag == True:
                #提示解压成功
                tkinter.messagebox.showinfo(title='提示', message='解压成功')
            # 关闭文件
            zp.close()
        else:
            #如果文件名后缀不是zip格式就提示错误
            tkinter.messagebox.showerror(title = '错误',message = '文件格式不正确')
    else:
        pass

#显示的界面布局
btn1 = tkinter.Button(text='添加文件', font=('楷体', 14), command=add_file)
btn1.place(x=20, y=20)

btn2 = tkinter.Button(text='压缩文件', font=('楷体', 14), command=zip_file)
btn2.place(x=120, y=20)

btn3 = tkinter.Button(text='解压文件', font=('楷体', 14), command=unzipfile)
btn3.place(x=220, y=20)

label1 = tkinter.Label(text='显示压缩文件路径', bg='white', anchor='nw', justify='left')
label1.place(x=20, y=80, width=300, height=260)

# 加入消息循环
root.mainloop()