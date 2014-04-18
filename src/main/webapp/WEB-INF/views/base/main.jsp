<!--
Created by IntelliJ IDEA.
UserEntity: zjy
Date: 1-3-2013
Time: 10:13
-->
<%@page contentType="text/html" pageEncoding="UTF-8" %>


<html lang="en">
<head>
    <meta name="description" content="Main Window of App"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <script type="text/javascript" src="/extjs/ext-all.js"></script>
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>

    <title>Main</title>
</head>

<body>
    <script type="text/javascript">
        (function () {
            Ext.onReady(function () {
                var topLogoPanel = Ext.create('Ext.panel.Panel', {
                    id: "topLogoPanel",
                    bodyStyle: {
                        background: '#3892D3'
                    },
                    border: false,
                    html: '<h1><div style="color:#ffffff;height:40px;line-height:40px;text-indent:60px">Web Application Development</div></h1>',
                    width: window.screen.width * 0.9
                });

                var topInfoPanel = Ext.create('Ext.panel.Panel', {
                    id: "topInfoPanel",
                    bodyStyle: {
                        background: '#3892D3'
                    },
                    border: false,
                    html: '<div style="color:#ffffff;height:40px;line-height:20px"><br/>junyan Zhang<br/>Ver: 1.21</div>',
                    width: window.screen.width * 0.1
                });

                var topBar = Ext.create('Ext.panel.Panel', {
                    id: "topBar",
                    region: 'north',
                    height: '30px',
                    border: false,
                    bodyStyle: {
                        background: '#3892D3'
                    },
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [topLogoPanel, topInfoPanel],
                    margin: '0 0 3 0'  //top, right, bottom, left
                });

                // may be this model data will be add to Ext.data.NodeInterface
                Ext.define('FuncTreeModel', {
                    extend: 'Ext.data.Model',
                    fields: [
                        {name: 'id', type: 'int'},
                        {name: 'text', type: 'string'},
                        {name: 'url', type: 'string'}
                    ]
                });

                var funcTreeStore = Ext.create('Ext.data.TreeStore', {
                    id: "funcTreeStore",
                    model: 'FuncTreeModel',
                    autoLoad: true,
                    root: {
                        root: true,
                        expanded: true,
                        expandable: true,
                        text: 'Root',
                        isFirst: true,
                        depth: 0,
                        id: 1000
                    },
                    proxy: {
                        type: 'ajax',
                        //url: 'js/testJson.js',
                        url: '/functionTree',
                        nodeParam: 'id',
                        reader: {
                            type: 'json',
                            root: 'msg',
                            successProperty: 'success'
                        }
                    }
                });

                var funcTreePanel = Ext.create('Ext.tree.Panel', {
                    id: "funcTreePanel",
                    region: 'west',
                    useArrows: true,
                    collapsible: true,
                    title: 'Navigation',
                    width: '20%',
                    split: true,
                    rootVisible: false,
                    store: funcTreeStore,
                    margin: '0 0 5 0',
                    listeners: {itemclick: onFuncTreeClick}
                });

                var mainTabPanel = Ext.create('Ext.tab.Panel', {
                    id: "mainTabPanel",
                    region: 'center',
                    activeTab: 0,      // First tab active by default
                    items: {
                        id: 'homeTab',
                        title: 'Home',
                        html: 'The first tab\'s content. Others may be added dynamically'
                    },
                    margin: '0 0 5 3',
                    listeners: {remove: onTabCardClose}
                });

                var mainViewPort = Ext.create('Ext.container.Viewport', {
                    layout: 'border',
                    items: [topBar, funcTreePanel, mainTabPanel],
                    renderTo: Ext.getBody()
                });

                Ext.tip.QuickTipManager.init();
                Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
                    maxWidth: 200,
                    minWidth: 100,
                    showDelay: 2000
                });
            });

            function onFuncTreeClick(view, record, item, index, e) {
                if (record.get('leaf') == true)
                    addNewTab(record.get('id'), record.get('text'));
            }

            function onTabCardClose(container, component) {
                component.destroy();
            }
        })();
    </script>
</body>
</html>