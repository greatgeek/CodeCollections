# Understanding Cookies and Sessions

## What is a "Cookie"?

A cookie is a small piece of text stored on a user's computer by their browser. Common uses for cookies are authentication, storing of site preferences, shopping cart items, and server session identification.

Each time the user's web browser interacts with a web server it will pass the cookie information to the web server. Only the cookies stored by the browser that relate to the domain in the requested URL will be sent to the server. This means that cookies that relate to www.example.com will not be sent to www.exampledomain.com.

In essence, a cookie is a great way of linking one page to the next for a user's interaction with a web site or web application.

## What is a "Session"?

A session can be defined as a server-side storage of information that is desired to persist throughout the user's interaction with the web site or web application.

Instead of storing large and constantly changing information via cookies in the user's browser, **only a unique identifier is stored on the client side** **(called a "session id").**  This session id is passed to the web server every time the browser makes an HTTP request (ie a page link or AJAX request). The web application pairs this session id with it's internal database and retrieves the stored variables for use by the requested page.

## **What are sessions? How do they work?**

Because HTTP is stateless, in order to associate a request to any other request, you need a way to store user data between HTTP requests.

Cookies or URL parameters ( for ex. like http://example.com/myPage?asd=lol&boo=no ) are both suitable ways to transport data between 2 or more request. However they are not good in case you don't want that data to be readable/editable on client side.

The solution is to store that data server side, give it an "id", and let the client only know (and pass back at every http request) that id. There you go, sessions implemented. Or you can use the client as a convenient remote storage, but you would encrypt the data and keep the secret server-side.

Of course there are other aspects to consider, like you don't want people to hijack other's sessions, you want sessions to not last forever but to expire, and so on.

In your specific example, the user id (could be username or another unique ID in your user database) is stored in the session data, server-side, after successful identification. Then for every HTTP request you get from the client, the session id (given by the client) will point you to the correct session data (stored by the server) that contains the authenticated user id - that way your code will know what user it is talking to.



## 参考

[What are sessions? How do they work?](https://stackoverflow.com/questions/3804209/what-are-sessions-how-do-they-work)

[Understanding Cookies and Sessions](http://www.lassosoft.com/Tutorial-Understanding-Cookies-and-Sessions)