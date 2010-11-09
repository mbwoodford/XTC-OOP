#!/bin/sh
# a shell script that runs Translator on a file,
# runs the file and its translated version, then uses unix sdiff
# tool to compare the output of both files
#
# for OOP fall 10 by The Group
clear
echo "Java to CPP Translation"
D="1";
while [ $D ]; do
echo "Enter the testing directory (return blank to quit):"
	read D
	if [ $D ]; then
		echo Cleaning directory $D/
		cd $D
		echo
		echo demo.java:
		more demo.java
		echo
		#clean out previous translations
		make -f ../Makefile clean
		cp ../java_lang.cpp ./
		cp ../java_lang.h ./
		make -f ../Makefile
		#sdiff will output both files to command line, more useful here than diff
		sdiff java.out.txt cpp.out.txt
		echo DONE
		cd ../
	else
		D=""
	fi
done