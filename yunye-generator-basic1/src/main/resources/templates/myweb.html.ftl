<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的官网</title>
</head>
<body>
<h1>欢迎来到我的官网</h1>
<ul>
    <#list menuItems as item>
        <li><a href="${item.url}">${item.label}</a></li>
    </#list>
</ul>
<footer>
    ${currentYear} 我的官网. All rights reserved
</footer>
</body>
</html>