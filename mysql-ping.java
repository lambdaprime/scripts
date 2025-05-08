/**
 * Copyright 2020 lambdaprime
 *
 * Pings MySQL server indefinitely and prints the response time.
 * Does not print consecutive response times which are equal.
 *
 * Requires jeval v26
 *
 */

void pingUntilOk(String ip, int port, String login, String pass) {
    while (true) {
        var r = new XExec("mysql",
                         "-h", ip,
                         "-u", login,
                         "-p" + pass,
                         "-P" + port,
                         "--connect-timeout=1",
                          "-e", "select 1").start().forwardStderrAsync(true);
        try {
            if (r.code().get() == 0) break;
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

void infinitePing(String ip, int port, String login, String pass) {
    long prev = 0;
    while (true) {
        var msec = new Microprofiler().measureRealTime(() -> pingUntilOk(ip, port, login, pass));
        if (Math.abs(prev - msec) > 5) {
            out.format("%d msec\n", msec);
            prev = msec;
        }
    }
}

infinitePing("127.0.0.1", 3306, "admin", "password");
