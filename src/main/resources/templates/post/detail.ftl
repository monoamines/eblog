





<#include '../inc/layout.ftl'>

<@layout "博客分类">
  <#include "../inc/header-panel.ftl"/>
<div class="layui-container">
  <div class="layui-row layui-col-space15">
    <div class="layui-col-md8 content detail">
      <div class="fly-panel detail-box">
        <h1>${onePost.title}</h1>
        <div class="fly-detail-info">
          <!-- <span class="layui-badge">审核中</span> -->
          <span class="layui-badge layui-bg-green fly-detail-column">${onePost.categoryName}</span>
          
<#--          <span class="layui-badge" style="background-color: #999;">未结</span>-->
          <!-- <span class="layui-badge" style="background-color: #5FB878;">已结</span> -->
          
          <#if onePost.level gt 0><span class="layui-badge layui-bg-black">置顶</span></#if>
        <#if onePost.recommend>  <span class="layui-badge layui-bg-red">精帖</span></#if>
          
          <div class="fly-admin-box" data-id="${onePost.id}">
            <#if (profile.id)??>
            <#if onePost.userId==profile.id>
            <span class="layui-btn layui-btn-xs jie-admin" type="del">删除</span>
            </#if>
            </#if>
               <@shiro.hasRole name="admin">
                 <span class="layui-btn layui-btn-xs jie-admin" type="set" field="delete" >删除</span>
                 <#if onePost.level==0>
            <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="1">置顶</span>
                 </#if>
                 <#if onePost.level gt 0>
            <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="0" style="background-color:#ccc;">取消置顶</span>
                 </#if>
                 <#if !onePost.recommend>
            <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="1">加精</span>
                 </#if>
                  <#if !onePost.recommend>
            <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="0" style="background-color:#ccc;">取消加精</span>
                 </#if>
               </@shiro.hasRole>
          </div>
          <span class="fly-list-nums"> 
            <a href="#comment"><i class="iconfont" title="回答">&#xe60c;</i> ${onePost.commentCount}</a>
            <i class="iconfont" title="人气">&#xe60b;</i> ${onePost.viewCount}
          </span>
        </div>
        <div class="detail-about">
          <a class="fly-avatar" href="../user/${onePost.userId}">
            <img src="${onePost.authorAvatar}" alt="贤心">
          </a>
          <div class="fly-detail-user">
            <a href="../user/home.html" class="fly-link">
              <cite>贤心</cite>
              <i class="iconfont icon-renzheng" title="认证信息：{{ rows.user.approve }}"></i>
              <i class="layui-badge fly-badge-vip">VIP3</i>
            </a>
            <span>${timeAgo(onePost.created)}</span>
          </div>
          <div class="detail-hits" id="LAY_jieAdmin" data-id="${onePost.id}">
            <span style="padding-right: 10px; color: #FF7200">悬赏：60飞吻</span>  
            <span class="layui-btn layui-btn-xs jie-admin" type="edit"><a href="/post/edit?id=${onePost.id}">编辑此贴</a></span>
          </div>
        </div>
        <div class="detail-body photos">
         ${onePost.content}
        </div>
      </div>

      <div class="fly-panel detail-box" id="flyReply">
        <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
          <legend>回帖</legend>
        </fieldset>

        <ul class="jieda" id="jieda">

          <#list comments.records as result>
            <li data-id="${result.id}" class="jieda-daan">
              <a name="item-${result.id}"></a>
              <div class="detail-about detail-about-reply">
                <a class="fly-avatar" href="/user/${onePost.userId}">
                  <img src="${onePost.authorAvatar}" alt=" ${onePost.authorName}">
                </a>
                <div class="fly-detail-user">
                  <a href="" class="fly-link">
                    <cite>${result.userName}</cite>
                    <i class="iconfont icon-renzheng" title="认证信息：XXX"></i>
                    <i class="layui-badge fly-badge-vip">VIP3</i>
                  </a>
                  <#if onePost.userId==result.userId>
                  <span>(楼主)</span>
                  </#if>
                  <!--
                  <span style="color:#5FB878">(管理员)</span>
                  <span style="color:#FF9E3F">（社区之光）</span>
                  <span style="color:#999">（该号已被封）</span>
                  -->
                </div>

                <div class="detail-hits">
                  <span>${timeAgo(result.created)}</span>
                </div>

                <i class="iconfont icon-caina" title="最佳答案"></i>
              </div>
              <div class="detail-body jieda-body photos">
               ${result.content}
              </div>
              <div class="jieda-reply">
              <span class="jieda-zan zanok" type="zan">
                <i class="iconfont icon-zan"></i>
                <em>${result.voteUp}</em>
              </span>
                <span type="reply">
                <i class="iconfont icon-svgmoban53"></i>
                回复
              </span>
                <div class="jieda-admin">
                  <span type="del">删除</span>
                  <!-- <span class="jieda-accept" type="accept">采纳</span> -->
                </div>
              </div>
            </li>
          </#list>

          
          <!-- 无数据时 -->
          <!-- <li class="fly-none">消灭零回复</li> -->
        </ul>
        <@paging comments></@paging>
        
        <div class="layui-form layui-form-pane">
          <form action="/post/reply/" method="post">
            <div class="layui-form-item layui-form-text">
              <a name="comment"></a>
              <div class="layui-input-block">
                <textarea id="L_content" name="content" required lay-verify="required" placeholder="请输入内容"  class="layui-textarea fly-editor" style="height: 150px;"></textarea>
              </div>
            </div>
            <div class="layui-form-item">
              <input type="hidden" name="jid" value="${onePost.id}">
              <button class="layui-btn" lay-filter="*" lay-submit>提交回复</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <#include "../inc/right.ftl"/>
  </div>
</div>
<script>
  layui.cache.page='jie';

  $(function () {
    layui.use(['fly', 'face'], function() {
      var fly = layui.fly;
      $('.detail-body').each(function(){
        var othis = $(this), html = othis.html();
        othis.html(fly.content(html));
      });
    });
  });

</script>
</@layout>



