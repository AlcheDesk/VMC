# VMC
**Virtual Machine Controller**


It is used to accept task commands from EMS server and execute those commands, send back the result to ATM server.

**Development requirements:**

Java 8
Gradle

**Generate the executable Jar:**

You only need to run the boorJar Gradle task.
gragle bootJar

It will garatne a Jar named VMC_API.jar. Please refer to the Docker project to see the location where to put the jar file.

This project is ctrated and developed in Eclipse IDE.

## System requirements

OS: Windows 7/8/10, Windows Server 2016
RAM: 4G
Open Firewall Port: 9100
softwares: Firefox Setup 54.0.1 and Chrome 68.0.3440.84
Java : Java 8

## Installtion Steps

1.  Disable autoupdate features on firefox and chrome
2.  Put VMCDrivers folder and belonging contetns on Desktop
3. Create a new folder named VMC_log on desktop
4.  Copy osgijar to C drive
5.  Copy VMC folder to Desktop, it should contents the following files
*VMC-1.4.jar
fileUploadAutoit_v1.exe
application.yml
configuration/config.ini
runVMC.bat*
6.  Create a shortcut of runVMC.bat to Desktop.
7.  Press Win+R, Enter "shell:startup" then Enter. Copy the shortcut just created to the opened window. This will make the shell autostart by windows.
8.  Modify appliaction.yml to what you needed.
- AMT and EMS server address and ports
- SMABA server address, username and password
- vmc.config.log.localvmclog (log folder, step 3)
- meowlomo.config.vmc.vpncontext = true (disable auto detect MAC address)
- meowlomo.config.vmc.hardcodeipaddress (input your own MAC address)

9. Swagger address http://<VMC virtual machine IP>:8100 when the swagger is enabled in the application.yml


## 虚拟机需求
系统:Windows 7/8/10， WINDOWS Server 2016
内存: 4G
防火墙： 开放9100端口

## 安装步骤

1.  software文件夹中安装软件:
Firefox Setup 54.0.1.exe
68.0.3440.84_chrome_installer.exe
7z1900-x64.exe
jdk-8u221-windows-x64.exe
npp.7.Installer.exe

2.  Firefox和Chrome最好设置禁止自动更新，这个忘了，要百度

3.  保证VMCDrivers及文件在桌面

4.  桌面新建一个文件夹，名为VMC_log

5.  保证C盘下有osgijar文件夹（及内部文件）

6.  保证VMC文件夹在桌面，这里面只需要五个文件（夹），分别是：
VMC-1.4.jar, fileUploadAutoit_v1.exe, application.yml, configuration/config.ini, runVMC.bat
以及osgijar文件夹，其中osgijar文件夹摆放的位置其实是configuration/config.ini中指定的，不变的话按第5点指定位置放

7.  将runVMC.bat生成快捷方式到桌面，复制生成的快捷方式，然后Win键 + R键，键入"shell:startup"，回车，在弹出的文件夹中粘贴快捷方式。这样重启就会运行这个bat脚本把VMC运行起来，并生成日期时间戳的log，注意bat脚本第一行的日期是通过截取系统时间生成的，英文系统可能不同，可以自行百度。这个主要替代之前艳芳的一个powershell脚本，那个太重了（依赖于较新的.net环境）。

8.  VMC文件夹中的application.yml修改。一般只需要改三处：
ATM与EMS地址，带端口
SAMBA地址与账密（直接搜索samba关键字），是meowlomo.config.service.file前缀的几个属性，如果是拷贝生成的，一般只需要修改地址
Log地址为：vmc.config.log.localvmclog，要有对应的文件夹，与第4步对应。
有的VMC的网络配置已经比较乱了的时间（java代码获取到的ip拿去EMS注册无法访问到），则修改meowlomo.config.vmc.vpncontext为true，并将meowlomo.config.vmc.hardcodeipaddress修改为VMC自身实际IP(EMS能够访问到的IP）

9. Swagger UI 地址为 http://<VMC虚拟机IP地址>:8100
