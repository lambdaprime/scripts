/*
 * lambdaprime-scripts
 *
 * See README.org
 *
 */

var hElements = document.getElementsByTagName('*')
var content = document.createElement('content')

function newContentEntry(name, styleName) {
    var div = document.createElement('div')
    var link = document.createElement('a')
    //newDiv.className = 'content-class'
    link.href = '#' + name
    div.className = styleName
    link.innerHTML = name
    div.appendChild(link)
    return div
}

for (var i = 0; i < hElements.length; i++) {
    var name = hElements[i].innerHTML
    if (hElements[i].nodeName == 'H1') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:history.back()"
        link.innerHTML = ">>>"
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h1link'))
        hElements[i].appendChild(link)
    } else if (hElements[i].nodeName == 'H2') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:history.back()"
        link.innerHTML = ">>>"
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h2link'))
        hElements[i].appendChild(link)
    } else if (hElements[i].nodeName == 'H3') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:history.back()"
        link.innerHTML = ">>>"
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h3link'))
        hElements[i].appendChild(link)
    } else if (hElements[i].nodeName == 'H4') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:history.back()"
        link.innerHTML = ">>>"
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h4link'))
        hElements[i].appendChild(link)
    } else if (hElements[i].nodeName == 'H5') {
        var link = document.createElement('a')
        link.name = hElements[i].innerHTML
        link.href = "javascript:history.back()"
        link.innerHTML = ">>>"
        content.appendChild(newContentEntry(hElements[i].innerHTML, 'h5link'))
        hElements[i].appendChild(link)
    }    
}

document.getElementsByTagName('content')[0].appendChild(content)
