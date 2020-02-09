/**
 * Copyright 2020 lambdaprime
 *
 * Pings MySQL server indefinitely and prints the response time.
 * Does not print consecutive response times which are equal.
 *
 * Requires jeval
 *
 */

void pingUntilOk(String ip, String login, String pass) {
    while (true) {
        var r = new Exec("mysql",
                         "-h", ip,
                         "-u", login,
                         "-p" + pass,
                         "--connect-timeout=1",
                         "-e", "select 1").run();
        try {
            if (r.code.get() == 0) break;
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

void infinitePing(String ip, String login, String pass) {
    long prev = 0;
    while (true) {
        var msec = new Microprofiler().measureRealTime(() -> pingUntilOk(ip, login, pass));
        if (Math.abs(prev - msec) > 5) {
            out.format("%d msec\n", msec);
            prev = msec;
        }
    }
}

infinitePing("172.16.2.100", "admin", "password");
