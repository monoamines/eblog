<#include "inc/layout.ftl"/>

<@layout "搜索">
<#include "inc/header-panel.ftl"/>


<div class="layui-container">
    <div class="layui-row layui-col-space15">

        <div class="layui-col-md8">
            <div class="fly-panel">
                <div class="fly-panel-title fly-filter">
                    <a>您正在搜索关键字“${q}” 一共有${pageData.totals}条记录</a>

                </div>
                <ul class="fly-list">
                    <#list pageData.records as data>
                        <@postlist data> </@postlist>
                    </#list>
                </ul>
                <@paging pageData></@paging>
            </div>
        </div>
        <#include "inc/right.ftl"/>

    </div>
</div>
</@layout>