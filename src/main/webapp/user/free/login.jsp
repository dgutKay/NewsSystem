<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!--bootstrap必须使用html5规范-->
<!DOCTYPE html>
<html lang="cn">
  <head>
    <title>login.jsp</title>

    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <!--Bootstrap必须设定 -->
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />

    <!--以下css和js都必须按此顺序引入 -->
    <!-- Bootstrap CSS -->
    <link
      rel="stylesheet"
      href="/NewsSystem/plugin/bootstrap-4.2.1-dist/css/bootstrap.min.css"
    />
    <!--Bootstrap必须先引入jquery -->
    <script
      type="text/javascript"
      src="/NewsSystem/js/jquery/jquery-3.3.1.min.js"
    ></script>
    <script src="/NewsSystem/plugin/bootstrap-4.2.1-dist/js/popper.min.js"></script>
    <script src="/NewsSystem/plugin/bootstrap-4.2.1-dist/js/bootstrap.min.js"></script>

    <script
      type="text/javascript"
      charset="utf-8"
      src="http://connect.qq.com/qc_jssdk.js"
      data-appid="101504585"
      data-redirecturi="http://220a27a0.nat123.cc:19537/news2/user/public/qqLoginCallback.jsp"
    ></script>

    <script type="text/javascript">
      $(document).ready(function() {
        $("#checkImg").on("click", function() {
          $(this).attr(
            "src",
            "/NewsSystem/servlet/ImageCheckCodeServlet?rand=" + Math.random()
          );
        });

        $("#submitform").on("submit", function(ev) {
          ev.preventDefault(); //阻止表单提交
          $.ajax({
            type: "post",
            dataType: "json",
            url: "/NewsSystem/servlet/UserServlet?condition=login",
            data: $("#submitform").serialize(),
            success: function(data) {
              if (data != null) {
                if (data.result == 1) {
                  window.location.href = data.redirectUrl; //跳转网页
                } else {
                  //登录失败
                  $("#myModalData").html(data.message);
                  //显示模态框
                  $("#myModal").modal("show");
                }
              }
            },
            error: function() {
              alert("登录失败!未能连接到服务器!");
            }
          });
        });
      });
    </script>
  </head>
  <body>
    <div class="container">
      <!--justify-content-center表示内部的子元素（子标签）居中     justify-content-center是Utilities的Flex工具之一-->
      <div class="row justify-content-center">
        <!--col-md-6表示只占一半的屏幕宽度 且居中（由于父div有justify-content-center） -->
        <div class="col-md-6">
          <!--card组件-->
          <div class="card">
            <div class="card-header">登录</div>
            <div class="card-body">
              <form id="submitform">
                <!--form-group row表示表单组且占一行（后面要把行细分为若干列）-->
                <div class="form-group row">
                  <!--col-md-3表示在md或更大屏幕上，label显示一行的4分之1宽  在更小屏幕上，该lable单独占一行-->
                  <label for="name" class="col-md-3 col-form-label"
                    >用户名：</label
                  >
                  <!--col-md-9表示在md或更大屏幕上，div显示一行的4分之3宽  在更小屏幕上，该div单独占一行-->
                  <div class="col-md-9">
                    <!--表单元素（标签）均应使用form-control样式-->
                    <input
                      type="text"
                      class="form-control"
                      id="name"
                      name="name"
                      placeholder="用户名"
                      required="required"
                      pattern="[a-z]([a-z0-9-_]){6,}([a-z0-9]+)"
                      title="用户名：至少需要8个字符，以字母开头，以字母或数字结尾，可以有-和_"
                    />
                  </div>
                </div>
                <div class="form-group row">
                  <label for="password" class="col-md-3 col-form-label"
                    >密码：</label
                  >
                  <div class="col-md-9">
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      name="password"
                      placeholder="密码"
                      required="required"
                      pattern="(\w){6,20}"
                      title="只能输入6-20个字母、数字、下划线"
                    />
                  </div>
                </div>
                <div class="form-group row">
                  <label for="checkCode" class="col-md-3 col-form-label"
                    >图形验证码：</label
                  >
                  <div class="col-md-4">
                    <input
                      type="text"
                      class="form-control"
                      name="checkCode"
                      id="checkCode"
                      placeholder="图形验证码"
                      required="required"
                    />
                  </div>
                  <!--img-fluid表示响应式图片 会自动变化高宽  见bootstrap文档的content中的images-->
                  <img
                    id="checkImg"
                    src="/NewsSystem/servlet/ImageCheckCodeServlet?rand=-1"
                    class="img-fluid"
                    style="cursor: pointer;"
                  />
                </div>
                <div class="form-group row">
                  <!--此div占位置用，并无内容-->
                  <div class="col-md-3"></div>
                  <div class="col-md-9">
                    <button type="submit" class="btn btn-primary mr-5">
                      提交
                    </button>
                    <span id="qqLoginBtn" class="mr-5"></span>
                    <script type="text/javascript">
                      QC.Login({
                        btnId: "qqLoginBtn", //插入按钮的节点id
                        showModal: true
                      });
                    </script>
                    <a href="findPassword.jsp">找回密码</a>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--模态框组件Modal-->
    <div id="myModal" class="modal " tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <!--模态框头部-->
          <div class="modal-header">
            <h5 class="modal-title">登录失败</h5>
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <!--模态框体  此处为空，通过jq代码为其添加内容-->
          <div class="modal-body" id="myModalData"></div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal">
              关闭
            </button>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
