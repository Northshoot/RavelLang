package org.stanford.ravel.rrt.utils;

import java.util.HashMap;

/**
 * Created by lauril on 2/1/17.
 */
public final class HttpStatus {
    static HashMap<Integer, String> status = new HashMap<>();
    
   private HttpStatus() {
       status.put(1, "EndPoint unreachable");
       //normal HTTP errors
       status.put(100, "Continue: Request received, please continue");
       status.put(101, "Switching Protocols: Switching to new protocol; obey Upgrade header");
       status.put(102, "Processing: WebDAV; RFC 2518");
       status.put(200, "OK: Request fulfilled, document follows");
       status.put(201, "Created: Document created, URL follows");
       status.put(202, "Accepted: Request accepted, processing continues off-line");
       status.put(203, "Non-Authoritative Information: Request fulfilled from cache");
       status.put(204, "No Content: Request fulfilled, nothing follows");
       status.put(205, "Reset Content: Clear input form for further input.");
       status.put(206, "Partial Content: Partial content follows.");
       status.put(207, "Multi-status: WebDAV; RFC 4918");
       status.put(208, "Already Reported: WebDAV; RFC 5842");
       status.put(226, "IM Used: RFC 3229");
       status.put(300, "Multiple Choices: Object has several resources -- see URI list");
       status.put(301, "Moved Permanently: Object moved permanently -- see URI list");
       status.put(302, "Found: Object moved temporarily -- see URI list");
       status.put(303, "See Other: Object moved -- see Method and URL list");
       status.put(304, "Not Modified: Document has not changed since given time");
       status.put(305, "Use Proxy: You must use proxy specified in Location to access this resource");
       status.put(306, "Switch Proxy: Subsequent requests should use the specified proxy");
       status.put(307, "Temporary Redirect: Object moved temporarily -- see URI list");
       status.put(308, "Permanent Redirect: Object moved permanently");
       status.put(400, "Bad Request: Bad request syntax or unsupported method");
       status.put(401, "Unauthorized: No permission -- see authorization schemes");
       status.put(402, "Payment Required: No payment -- see charging schemes");
       status.put(403, "Forbidden: Request forbidden -- authorization will not help");
       status.put(404, "Not Found: Nothing matches the given URI");
       status.put(405, "Method Not Allowed: Specified method is invalid for this resource.");
       status.put(406, "Not Acceptable: URI not available in preferred format.");
       status.put(407, "Proxy Authentication Required: You must authenticate with this proxy before proceeding.");
       status.put(408, "Request Timeout: Request timed out; try again later.");
       status.put(409, "Conflict: Request conflict.");
       status.put(410, "Gone: URI no longer exists and has been permanently removed.");
       status.put(411, "Length Required: Client must specify Content-Length.");
       status.put(412, "Precondition Failed: Precondition in headers is false.");
       status.put(413, "Request Entity Too Large: Entity is too large.");
       status.put(414, "Request-URI Too Long: URI is too long.");
       status.put(415, "Unsupported Media Type: Entity body in unsupported format.");
       status.put(416, "Requested Range Not Satisfiable: Cannot satisfy request range.");
       status.put(417, "Expectation Failed: Expect condition could not be satisfied.");
       status.put(418, "I'm a teapot: The HTCPCP server is a teapot");
       status.put(419, "Authentication Timeout: previously valid authentication has expired");
       status.put(420, "Method Failure / Enhance Your Calm: Spring Framework / Twitter");
       status.put(422, "Unprocessable Entity: WebDAV; RFC 4918");
       status.put(423, "Locked: WebDAV; RFC 4918");
       status.put(424, "Failed Dependency / Method Failure: WebDAV; RFC 4918");
       status.put(425, "Unordered Collection: Internet draft");
       status.put(426, "Upgrade Required: client should switch to a different protocol");
       status.put(428, "Precondition Required: RFC 6585");
       status.put(429, "Too Many Requests: RFC 6585");
       status.put(431, "Request Header Fields Too Large: RFC 6585");
       status.put(440, "Login Timeout: Microsoft");
       status.put(444, "No Response: Nginx");
       status.put(449, "Retry With: Microsoft");
       status.put(450, "Blocked by Windows Parental Controls: Microsoft");
       status.put(451, "Unavailable For Legal Reasons: Internet draft");
       status.put(494, "Request Header Too Large: Nginx");
       status.put(495, "Cert Error: Nginx");
       status.put(496, "No Cert: Nginx");
       status.put(497, "HTTP to HTTPS: Nginx");
       status.put(498, "Token expired/invalid: Esri");
       status.put(499, "Client Closed Request: Nginx");
       status.put(500, "Internal Server Error: Server got itself in trouble");
       status.put(501, "Not Implemented: Server does not support this operation");
       status.put(502, "Bad Gateway: Invalid responses from another server/proxy.");
       status.put(503, "Service Unavailable: The server cannot process the request due to a high load");
       status.put(504, "Gateway Timeout: The gateway server did not receive a timely response");
       status.put(505, "HTTP Version Not Supported: Cannot fulfill request.");
       status.put(506, "Variant Also Negotiates: RFC 2295");
       status.put(507, "Insufficient Storage: WebDAV; RFC 4918");
       status.put(508, "Loop Detected: WebDAV; RFC 5842");
       status.put(509, "Bandwidth Limit Exceeded: Apache bw/limited extension");
       status.put(510, "Not Extended: RFC 2774");
       status.put(511, "Network Authentication Required: RFC 6585");
       status.put(598, "Network read timeout error: Unknown");
       status.put(599, "Network connect timeout error: Unknown");
    }

       public static String getError(int hstatus){
           return status.get(hstatus);
        }
}
