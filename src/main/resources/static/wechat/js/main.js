function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if (r != null) return unescape(r[2]);
    return null;
}

//改变导航条样式
function removeOtherActive(obj) {
    //让导航条的每一个元素 熄灭(图片,字体从绿 -> 灰)
    $(".weui-tabbar a.weui-tabbar__item").each(function () {
        if ($(this).hasClass("weui-bar__item--on")) {
            $(this).removeClass("weui-bar__item--on");
            let img = $(this).find("img");
            let src = img.attr("src").replace("3", "-unsel3");
            $(img).attr("src", src);
        }
    });

    $(obj).addClass("weui-bar__item--on");
    let new_img = $(obj).find("img");
    let replacedImageSrc = new_img.attr("src").replace("-unsel3", "3");
    new_img.attr("src", replacedImageSrc);
}

function getWechatUserInfo(callback, baseUrl,appId) {
    // var referrer = getUrlParam("referrer");

    // if (!referrer) {
    //     referrer = null;
    // }
    var redirect_url = "http://" + baseUrl + "/auth";
    // console.log("referrer",referrer);
    console.log("redirect_url", redirect_url);
    // var appid = "wx9de8d36f3e4d901e";
    var response_type = "code";
    var scope = "snsapi_userinfo";
    var state = "123";

    var url = "https://open.weixin.qq.com/connect/oauth2/" +
        "authorize?appid=" + appId +
        "&redirect_uri=" + location.href.split('#')[0] +
        "&response_type=" + response_type +
        "&scope=" + scope +
        "&state=" + state +
        "#wechat_redirect";
    //获得微信服务器发来的code
    var code = getUrlParam("code");

    if (!code) {
        window.location.href = url;
    } else {
        $.showLoading("正在授权...");
        $.ajax({
            type: 'get',
            url: redirect_url,
            dataType: 'json',
            data: {
                code: code,
                state: "123",
                // referrer: referrer,
            },
            success: function (data) {
                if (data.state === 1) {
                    callback(data.data);
                    $.hideLoading();
                }
            },
        })
    }
}
