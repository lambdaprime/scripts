/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 */

var hElements = document.getElementsByTagName('*')
var content = document.createElement('content')
var prevPosition = 0;

function newContentEntry(name, styleName) {
    var link = document.createElement('a')
    link.innerHTML = name
    link.setAttribute('href', '#' + name);
    link.setAttribute('onclick', "savePosition()");
    var div = document.createElement('div')
    div.className = styleName
    div.onclick = function () {  }
    div.appendChild(link)
    return div
}

function savePosition() {
    prevPosition = document.body.scrollTop;
    return false;
}

function goBack() {
    document.body.scrollTop = prevPosition;
}

for (var i = 0; i < hElements.length; i++) {
    var name = hElements[i].innerHTML
    if (hElements[i].nodeName == 'H1') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:goBack()"
        link.innerHTML = hElements[i].innerHTML
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h1link'))
        hElements[i].replaceChild(link, hElements[i].childNodes[0])
    } else if (hElements[i].nodeName == 'H2') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:goBack()"
        link.innerHTML = hElements[i].innerHTML
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h2link'))
        hElements[i].replaceChild(link, hElements[i].childNodes[0])
    } else if (hElements[i].nodeName == 'H3') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:goBack()"
        link.innerHTML = hElements[i].innerHTML
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h3link'))
        hElements[i].replaceChild(link, hElements[i].childNodes[0])
    } else if (hElements[i].nodeName == 'H4') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:goBack()"
        link.innerHTML = hElements[i].innerHTML
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h4link'))
        hElements[i].replaceChild(link, hElements[i].childNodes[0])
    } else if (hElements[i].nodeName == 'H5') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:goBack()"
        link.innerHTML = hElements[i].innerHTML
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h5link'))
        hElements[i].replaceChild(link, hElements[i].childNodes[0])
    }
    
}
document.getElementsByTagName('content')[0].appendChild(content)
