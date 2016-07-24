<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	 <h1>Login to Web App</h1>
      <form method="post" action="signInProc">
        <p><input type="text" name="login" placeholder="Username or Email"></p>
        <p><input type="password" name="password" placeholder="Password"></p>
        <p>
          <label>
            <input type="checkbox" name="remember_me" id="remember_me">
            Remember me on this computer
          </label>
        </p>
        <p><input type="submit" name="commit" value="Sign In"></p>
        
      </form>
      <p><a href="signUp">SignUp</a></p>
</body>
</html>