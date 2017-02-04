#
# See README.org
#

if [ $# -ne 2 ]; then
    echo "Usage: $(basename $0) <PROJECT_DIR> <OUT_DIR>"
    exit 1
fi

PROJECT=$1
OUT=$2

cd $PROJECT
echo "Parsing git stat"
git log --name-only | grep "^[^ |commit|Date|Author]" | sort | uniq -c | sort -nr > $OUT/files

echo "Gathering results"
cd $OUT
cat files | grep "\.xml" | sort -nr > files_xml.txt
cat files | grep "\.java" | sort -nr > files_java.txt
cat files | grep "\.js" | sort -nr > files_js.txt

