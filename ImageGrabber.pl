#! D:\Perl\bin\perl

# lambdaprime-scripts
#
# See README.org
#

# *** FILL DATA BELOW ***

my @urls = (
    # *** PUT URLS HERE ***
    #"http://images.com/photo/1",
);

$storage = "/path/where/to/store";

# ******

$out = "/tmp/out";

$DIR = "";
$content = "";

foreach $url (@urls) {

    print "Current url is: $url\n";

    $content = get $url;

    select STDOUT; $| = 1;

    fail("Couldn't get $url\n") unless defined $content;

#open(OUT, ">$out") or die "Unable to open file $out: $!\n";

    my $pos = 0;

    open(OUT, ">$out") or die "Unable to open file $out: $!\n";
    print OUT $content;
    close(OUT);

    while (($pos = index($content, "phtTdMain", $pos)) != -1) {
	$s = index($content, "href=", $pos);
	fail("Unable to extract entryID start url\n") unless $s != -1;
	$s += 6;
	$e = index($content, "\"", $s);
	fail("Unable to extract entryID url\n") unless $e != -1;
	#print $s." ".$e."\n";
	$entryIdUrl = substr($content, $s, $e - $s)."\n";
	print $entryIdUrl."\n";
	getImageInRealSizeUrl($entryIdUrl);
	$pos = $e;
    }
}

sub getImageInRealSizeUrl {
    my ($url) = @_;
    my $errMsg = "Error: getImageInRealSize: ";
    my $content = get $url;
    fail("${errMsg} Couldn't get $url")
	unless defined $content;
    my $pos = index($content, "catNameActive");
    fail("${errMsg}Couldn't find catNameActive in $url")
	unless $pos != -1;
    my $s = $pos + 15;
    my $e = index($content, "<", $s);
    fail("${errMsg}Couldn't extract url from catNameActive in $url")
	unless $e != -1;
    my $title = substr($content, $s, $e - $s);
    $title =~ s/\&quot\;//g;
    $title =~ s/:/ /g;
    $title =~ s/\?//g;
    $DIR = $storage."/$title";
    print "Title is : ".$title."\n";
    mkdir($DIR);

    $pos = index($content, "s5227");
    if ($pos == -1) {
	$pos = index($content, "phtmSpan35");
	fail("${errMsg}Couldn't find phtmSpan35 in $url")
	    unless $pos != -1;
	$s = index($content, "src=", $pos);
	fail("${errMsg}Couldn't find src= in $url")
	    unless $s != -1;
	$s += 5;
	$e = index($content, "\"", $s);
	fail("${errMsg}Couldn't extract url from src= in $url")
	    unless $e != -1;
	my $imageInRsUrl = substr($content, $s, $e - $s);
	print "Image in real size url is: ".
	    $imageInRsUrl."\n";
	getImage($imageInRsUrl);
	#open(OUT, ">$out") or die "Unable to open file $out: $!\n";
	#print OUT $content;
	#close(OUT);
	#fail("${errMsg}Couldn't find s5227 in $url")
	#    unless $pos != -1;
    } else {
	$s = rindex($content, "href=", $pos);
	fail("${errMsg}Couldn't find href= in $url")
	    unless $s != -1;
	$s += 6;
	$e = index($content, "\"", $s);
	fail("${errMsg}Couldn't extract url from href=".
	     "in $url") unless $e != -1;
	my $imageInRsUrl = substr($content, $s, $e - $s);
	print "Image in real size url is: ".
	    $imageInRsUrl."\n";
	getImageUrl($imageInRsUrl, $DIR);
    }
#    $pos = 0;
#    while (($pos = index($content, "photoOtherLink", $pos))
#	!= -1) 
#    {
#	$s = $pos + 22;
#	$e = index($content, "\"", $s);
#	die "${errMsg} Couldn't extract photoOtherLink url from in $url"
#	    unless $e != -1;
#	my $photoOtherLink = substr($content, $s, $e - $s);
#	print "photoOtherLink is: ".$photoOtherLink."\n";
#	my $imagePageContent = get $photoOtherLink;
#	die "${errMsg} Couldn't get $photoOtherLink"
#	    unless defined $imagePageContent;
#	getImageInRealSizeUrl($imagePageContent);
#	$pos = $e;
#    }

    #open(OUT, ">$out") or die "Unable to open file $out: $!\n";
    #print OUT $content;
    #close(OUT);
}

sub getImageUrl {
    my ($url) = @_;
    my $errMsg = "Error: getImage: ";
    my $content = get $url;
    fail("${errMsg}Couldn't get $url")
	    unless defined $content;
    my $s = index($content, "src", 0);
    fail("${errMsg}Couldn't find src in $url")
	unless $s != -1;
    $s += 5;
    my $e = index($content, "\"", $s);
    fail("${errMsg}Couldn't extract url from href= in $url")
	unless $e != -1;
    my $imageUrl = substr($content, $s, $e - $s);
    print "Image url is: ".$imageUrl."\n";
    getImage($imageUrl);
}

sub getImage {
    my ($url) = @_;
    my $content = get $url;
    fail("Couldn't get $url")
	    unless defined $content;
    my $p = rindex($url, "/");
    fail("Couldn't extract file name from $url")
	unless $p != -1;
    $fileName = $DIR;
    $fileName .= substr($url, $p);
    print "Saving into: $fileName\n";
    open(OUT, ">$fileName") or
	fail("Unable to open file $fileName: $!\n");
    binmode OUT;
    print OUT $content;
    close(OUT);
}

sub fail {
    my ($errMsg) = @_;
    print $errMsg;
    exit 1;
}

#print OUT $content;
#close(OUT);
