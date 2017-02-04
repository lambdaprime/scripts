#! D:\Perl\bin\perl

#
# See README.org
#

if (@ARGV < 1) {
    print "please specify html input file name";
    exit -1;
}

$head = "";
if (-e "head") {
    open(HEAD, "head") or die "Unable to open file head: $!\n";
    print "Reading head\n";
    while (<HEAD>) {
        $head .= $_;
    }
}

$tail = "";
if (-e "tail") {
    open(TAIL, "tail") or die "Unable to open file tail: $!\n";
    print "Reading tail\n";
    while (<TAIL>) {
        $tail .= $_;
    }
}

$file = @ARGV[0];
open(FILE, "$file") or die "Unable to open $file: $!\n";
open(INDEX, ">index.html") or die "Unable to open index.html: $!\n";

$content = 0;

print INDEX $head;

$curHeader = 0;
$body = 0;

while (<FILE>) {
    if (/^<.?content>$/) {
        if ($content) {
            $curHeader = 0;
            $body = 1;
            print INDEX $tail;
        }
        $content = $content? 0: 1;
        next;
    }
    if ($content) {
        if (/class="h1link"/) {
            chomp;
            $curHeader++;
            $_ =~ s/href=\"\#(.*)\">/href=\"$curHeader\.html\">/;
            print INDEX;
            open(OUT, ">$curHeader.html") or die "Unable to open $outFileName: $!\n";
        } elsif (/class="h.link"/) {
            chomp;
            $_ =~ s/href=\"\#(.*)\">/href=\"$curHeader\.html\#\1\">/;
            print INDEX;
            print INDEX "\n";
        } else {
            print OUT;
        }
    }
    if ($body) {
        if (/<h1>.*<\/h1>/) {
            if (defined fileno OUT) {
                print OUT $tail;
            }
            $curHeader++;
            open(OUT, ">$curHeader.html") or die "Unable to open $outFileName: $!\n";
            print OUT $head;

        } else {
            print OUT;
        }
        
    }

}

print INDEX "</html>\n";
