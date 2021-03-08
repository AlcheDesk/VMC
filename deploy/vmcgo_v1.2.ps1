$currentTime=([DateTimeOffset](Get-Date)).ToString("yyyy-MM-dd-HHmmss")
$vmcLogfile='C:\Users\tester\Desktop\VMC1.3\log\result'+$currentTime+'.txt'
Function vmcRunComand
{
	Start-Process java -ArgumentList '-Dfile.encoding=utf-8 -jar', 'C:\Users\tester\Desktop\VMC1.3\VMC-1.4.jar' ` -RedirectStandardOutput $vmcLogfile
}

$runDir_current=Split-Path -Parent $MyInvocation.MyCommand.Definition
$logFile="$runDir_current\vmcgo.log"

Function LogWrite
{
   [CmdletBinding()]
    Param(
    [Parameter(Mandatory=$False)]
    [ValidateSet("[INFO]","[WARN]","[ERROR]","[FATAL]","[DEBUG]")]
    [String]
    $Level = "INFO",

    [Parameter(Mandatory=$True)]
    [string]
    $Message,

    [Parameter(Mandatory=$false)]
    [string]
    $logfile
    )

    $Stamp = (Get-Date).toString("yyyy/MM/dd HH:mm:ss")
    $Line = "$Stamp $Level $Message"
    If($logfile) {
        Add-Content $logfile -Value $Line
    }
    Else {
        Write-Output $Line
    }
}

$jarFile="VMC-1.4"
$jarFileName="$jarFile.jar"
$tempFileName="C:\jars\"
$current_UnixTimeStamp=([DateTimeOffset](Get-Date)).ToString("yyyy-MM-dd-HHmmss")
LogWrite "[INFO]" "=========================it's a log for vmcgo [log begin]========================" $logFile

if (Test-Path $tempFileName$jarFileName) {
	$tempFile="C:\jars\$jarFileName"
	LogWrite "[INFO]" "file does exist,let's do something for update" $logFile
	$tempJar_modifyTime=(Get-Item $tempFile).LastWriteTime
	$runJar_modifyTime=(Get-Item $runDir_current\$jarFileName).LastWriteTime
	LogWrite "[INFO]" "tempfile jarfile modify time is: $tempJar_modifyTime " $logFile 
	LogWrite "[INFO]" "runfile jarfile modify time is : $runJar_modifyTime " $logFile 
	if($tempJar_modifyTime -gt $runJar_modifyTime){		
		LogWrite "[INFO]" "Latest documents detected,Will be updated." $logFile 
		Rename-Item $runDir_current\$jarFileName $runDir_current\$jarFile$current_UnixTimeStamp.jar
		LogWrite "[INFO]" "[rename bak] oldName is: $runDir_current\$jarFileName;newName is: $runDir_current\$jarFile$current_UnixTimeStamp.jar" $logFile 
		Copy-Item $tempFile $runDir_current\$jarFileName
		LogWrite "[INFO]" "copy ok;and now get runjarFIle time is: $((Get-Item $runDir_current\$jarFileName).LastWriteTime)" $logFile		
		#Dir *.jar				
		vmcRunComand		
		LogWrite "[INFO]" "[update OK][run sucessed] vmc already update.and run sucessed" $logFile
	}
	else{
		LogWrite "[WARN]" "it's already lastest file,needn't update jar. just run vmc" $logFile
		vmcRunComand
		LogWrite "[INFO]" "[NO updates][run sucessed] vmc has no updates. but run sucessed" $logFile
	}
	
} else {
	LogWrite "[WARN]" "file [vmcjar] doesn't exist! needn't update jar. just run vmc" $logFile
	vmcRunComand
	LogWrite "[INFO]" "[NO updates][run sucessed] vmc has no updates. but run sucessed" $logFile
}
LogWrite "[INFO]" "==========================it's a log for vmcgo [log end]=========================" $logFile
LogWrite "[INFO]" "---------------------------------------------------------------------------------" $logFile