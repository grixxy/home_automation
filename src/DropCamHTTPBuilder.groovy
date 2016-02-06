import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method
import groovyx.net.http.RESTClient

import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
/**
 * Created by gregory on 2/4/16.
 */


def postBody = [username: 'grixxy@gmail.com', password: 'Sundmad@1975']

def http = new HTTPBuilder('https://home.nest.com')

def global_cookies = ''
def global_auth_params = ''

println "Overall NEST Login"


http.post(path: '/user/login', body: postBody,
        requestContentType: URLENC ) { resp, reader ->


    println "Overall NEST Login: response status: ${resp.statusLine}"
    println 'Overall NEST Login: Headers: -----------'
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }

    global_cookies = resp.headers['Set-Cookie'].getValue()

    println 'Overall NEST Login: cookies: -----'
    println global_cookies
    println 'Overall NEST Login: response data: -----'
    System.out << reader
    println '\n--------------------'
    global_auth_params = reader
}

println "Drop Cam login"


postBody = [access_token: global_auth_params.access_token]
requestHeaders = [
        "Accept":"*/*",
        "Accept-Encoding":"gzip, deflate",
        "Accept-Language":"en-US,en;q=0.8,ru;q=0.6",
        "Connection":"keep-alive",
        "Content-Type":"application/x-www-form-urlencoded; charset=UTF-8",
        "Cookie":global_cookies,
        "Host":"home.nest.com",
        "Origin":"https://home.nest.com",
        "Referer":"https://home.nest.com/",
        "User-Agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36",
        "X-Requested-With":"XMLHttpRequest"
]



http.post(path: '/dropcam/api/login', body: postBody,
        requestContentType: URLENC, headers: requestHeaders ) { resp, reader ->

    println "Drop Cam login: response status: ${resp.statusLine}"
    println 'Drop Cam login: Headers: -----------'
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }
    global_cookies =global_cookies+ "; " + resp.headers['Set-Cookie'].getValue();
    println 'Drop Cam login: cookies'
    println global_cookies
    println 'Drop Cam login: Response data: -----'
    System.out << reader
    println '\n--------------------'

    println '\n--------------------'
}

println "List of Drop Cameras"

requestHeaders = [
        'Accept':"*/*",
        "Accept-Encoding":"gzip, deflate, sdch",
        "Accept-Language":"en-US,en;q=0.8,ru;q=0.6",
        "Connection":"keep-alive",
        "Cookie":global_cookies,
        "Host":"home.nest.com",
        "Referer":"https://home.nest.com/",
        "User-Agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36",
        "X-Requested-With":"XMLHttpRequest"
]

def camera_info

http.get(path: 'dropcam/api/cameras',
        requestContentType: URLENC, headers: requestHeaders ) { resp, reader ->

    println "List of Drop Cameras: response status: ${resp.statusLine}"
    println 'Headers: -----------'
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }

    println 'Response data: -----'
    System.out << reader
    camera_info = reader
    println '\n--------------------'

}

println "Turn it on!"

requestHeaders = [
        "Accept":"*/*",
        "Accept-Encoding":"gzip, deflate",
        "Accept-Language":"en-US,en;q=0.8,ru;q=0.6",
        "Connection":"keep-alive",
        "Content-Type":"application/x-www-form-urlencoded; charset=UTF-8",
        "Cookie":global_cookies,
        "Host":"home.nest.com",
        "Origin":"https://home.nest.com",
        "Referer":"https://home.nest.com/",
        "User-Agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36",
        "X-Requested-With":"XMLHttpRequest"
]

uuid = camera_info[0].get('uuid')
key = 'streaming.enabled'
turnCameraOn = 'false'

postBody = [uuid:uuid, key: key, value:turnCameraOn]

url = "https://home.nest.com/dropcam/api/cameras/${uuid}/properties"

http.post(path: url, body: postBody,
        requestContentType: URLENC, headers: requestHeaders ) { resp, reader ->

    println "Turn it on!: response status: ${resp.statusLine}"
    println 'Turn it on!: Headers: -----------'
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }
    println 'Turn it on!: cookies'
    println global_cookies
    println 'Turn it on!: Response data: -----'
    System.out << reader
    println '\n--------------------'

    println '\n--------------------'
}