# check source dir arg
if [ -z "$1" ]; then
  echo "usage: jscdoc -d targetdir file1.jsc file2.jsc ..."
fi
java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -cp ./output/lib/jscdoc.jar:../thirdparty/sun/j2se/lib/tools.jar:../jscc/output/lib/jscc.jar com/coremedia/tools/jscdoc/Main $1 $2 $3 $4 $5 $6 $7 $8 $9 

