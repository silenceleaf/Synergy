<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="description" content="Authority Management"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <script type="text/javascript" src="/extjs/ext-all-dev.js"></script>
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <title>Authority Management</title>
</head>
<body>
<div id='function_1008' style="width: 100%; height: 100%">
<script type="text/javascript">
    (function () {
        function initPage() {
            var templateStore = createTemplateStore('10013_10006');
            templateStore.load();
            templateStore.on ('load', function () {
                initComponent();
            });
        }

        function initComponent () {
            var templateStore = Ext.getStore('templateStore_' + '10013_10006');

            var roleGrid = Ext.create('Ext.grid.Panel', {
                title: 'Role Grid',
                id: 'roleGrid_1008_1',
                store: createDataStore('10013_1'),
                autoScroll: true,
                region: 'center',
                width: '30%',
                margin: '0 0 5 0',
                columns: [{
                    text: templateStore.getById(435).get('text'),
                    dataIndex: templateStore.getById(435).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(436).get('text'),
                    dataIndex: templateStore.getById(436).get('name'),
                    flex: 2
                }],
                listeners: {itemclick: onRoleGridClick}
            });

            Ext.define('FunctionModel_1008', {
                extend: 'Ext.data.Model',
                fields: [
                    {name: templateStore.getById(384).get('name'), type: 'string'},
                    {name: templateStore.getById(389).get('name'), type: 'string'},
                    {name: templateStore.getById(377).get('name'), type: 'string'},
                    {name: templateStore.getById(386).get('name'), type: 'string'},
                    {name: templateStore.getById(379).get('name'), type: 'string'},
                    {name: templateStore.getById(380).get('name'), type: 'string'},
                    {name: templateStore.getById(381).get('name'), type: 'string'},
                    {name: templateStore.getById(382).get('name'), type: 'string'},
                    {name: templateStore.getById(385).get('name'), type: 'string'},
                    {name: templateStore.getById(387).get('name'), type: 'string'},
                    {name: templateStore.getById(388).get('name'), type: 'string'}
                ]
            });

            var functionStore = Ext.create ('Ext.data.Store', {
                storeId: 'functionStore_1008',
                model: 'FunctionModel_1008',
                autoDestroy: true,
                proxy: {
                    method: 'GET',
                    type: 'ajax',
                    limitParam: undefined,
                    pageParam: undefined,
                    startParam: undefined,
                    url: '/base/authority/getAuthority',
                    reader: {
                        type: 'json',
                        root: 'msg',
                        successProperty: 'success'
                    }
                }
            });

            var functionGrid = Ext.create('Ext.grid.Panel', {
                title: 'Function Grid',
                id: 'dataGrid_1008_2',
                store: functionStore,
                autoScroll: true,
                width: '70%',
                split: true,
                region: 'east',
                margin: '0 0 5 0',
                columnWidth: 100,
                columns: [{
                    text: templateStore.getById(384).get('text'),
                    dataIndex: templateStore.getById(384).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(389).get('text'),
                    dataIndex: templateStore.getById(389).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(377).get('text'),
                    dataIndex: templateStore.getById(377).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(386).get('text'),
                    dataIndex: templateStore.getById(386).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(379).get('text'),
                    dataIndex: templateStore.getById(379).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(380).get('text'),
                    dataIndex: templateStore.getById(380).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(381).get('text'),
                    dataIndex: templateStore.getById(381).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(382).get('text'),
                    dataIndex: templateStore.getById(382).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(385).get('text'),
                    dataIndex: templateStore.getById(385).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(387).get('text'),
                    dataIndex: templateStore.getById(387).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(388).get('text'),
                    dataIndex: templateStore.getById(388).get('name'),
                    flex: 1
                }],
                dockedItems: [createToolBar('dataGrid_1008_1', 9, buttonEventDispatcher)]
            });

            var createRoleWindow = Ext.create('Ext.window.Window', {
                title: 'Create Role',
                id: 'createRoleWindow_1008',
                titleAlign: 'center',
                modal: true,
                height: 200,
                width: 600,
                resizable: false,
                closable: false,
                layout: {
                    type: 'vbox',
                    align: 'center',
                    pack: 'center'
                },
                items: [{
                    id: 'newRoleNameTextField',
                    xtype: 'textfield',
                    width: 400,
                    name: 'roleName',
                    fieldLabel: 'Role name',
                    allowBlank: false
                }],
                dockedItems: [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    defaults: {minWidth: 150},
                    items: [
                        {
                            xtype: 'tbfill'
                        }, {
                            xtype: 'button',
                            text: 'Cancel',
                            listeners: {
                                click: function() {
                                    var selectRolePopupWindow = Ext.getCmp('createRoleWindow_1008');
                                    selectRolePopupWindow.hide();
                                }
                            }
                        }, {
                            xtype: 'button',
                            text: 'OK',
                            listeners: {
                                click: function() {
                                    var textField = Ext.getCmp('newRoleNameTextField');
                                    if (textField.isValid()) {
                                        Ext.Ajax.request({
                                            url: '/base/authority/createRole',
                                            params: {roleName: textField.getValue()},
                                            success: function (response, opts) {
                                                var roleStore = Ext.getStore('dataGridStore_10013_1');
                                                roleStore.load();
                                                var selectRolePopupWindow = Ext.getCmp('createRoleWindow_1008');
                                                selectRolePopupWindow.hide();
                                            },
                                            failure: function (response, opts) {
                                                Ext.Msg.alert('Failure', 'Create role failure.');
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    ]
                }]
            });

            var addAuthorityStore = Ext.create ('Ext.data.Store', {
                storeId: 'addAuthorityStore_1008',
                model: 'FunctionModel_1008',
                autoDestroy: true,
                proxy: {
                    method: 'GET',
                    type: 'ajax',
                    limitParam: undefined,
                    pageParam: undefined,
                    startParam: undefined,
                    url: '/base/authority/getAvailableAuthority',
                    reader: {
                        type: 'json',
                        root: 'msg',
                        successProperty: 'success'
                    }
                }
            });

            var addAuthorityGrid = Ext.create('Ext.grid.Panel', {
                title: 'Add Authority Grid',
                id: 'dataGrid_1008_3',
                store: addAuthorityStore,
                autoScroll: true,
                columnWidth: 100,
                region: 'center',
                columns: [{
                    text: templateStore.getById(384).get('text'),
                    dataIndex: templateStore.getById(384).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(389).get('text'),
                    dataIndex: templateStore.getById(389).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(377).get('text'),
                    dataIndex: templateStore.getById(377).get('name'),
                    flex: 1
                }, {
                    text: templateStore.getById(386).get('text'),
                    dataIndex: templateStore.getById(386).get('name'),
                    flex: 1
                }]
            });

            var addAuthorityWindow = Ext.create('Ext.window.Window', {
                title: 'Add Authority',
                id: 'addAuthorityWindow_1008',
                titleAlign: 'center',
                modal: true,
                height: 400,
                width: 800,
                resizable: false,
                closable: false,
                layout: 'border',
                items: [addAuthorityGrid],
                dockedItems: [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    defaults: {minWidth: 150},
                    items: [
                        {
                            xtype: 'tbfill'
                        }, {
                            xtype: 'button',
                            text: 'Cancel',
                            listeners: {
                                click: function() {
                                    var addAuthorityWindow = Ext.getCmp('addAuthorityWindow_1008');
                                    addAuthorityWindow.hide();
                                }
                            }
                        }, {
                            xtype: 'button',
                            text: 'OK',
                            listeners: {
                                click: function() {
                                    var selectedRoleId = roleGrid.getSelectionModel().getSelection()[0].get('id');
                                    var selectedFunctionId = addAuthorityGrid.getSelectionModel().getSelection()[0].get('id');
                                    if (selectedRoleId && selectedFunctionId) {
                                        Ext.Ajax.request({
                                            url: '/base/authority/addAuthority',
                                            params: {roleId: selectedRoleId, functionId: selectedFunctionId},
                                            success: function (response, opts) {
                                                var functionStore = Ext.getStore('functionStore_1008');
                                                functionStore.load();
                                                var addAuthorityWindow = Ext.getCmp('addAuthorityWindow_1008');
                                                addAuthorityWindow.hide();
                                            },
                                            failure: function (response, opts) {
                                                Ext.Msg.alert('Failure', 'Add Authority failure.');
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    ]
                }]
            });

            var mainPanel = Ext.create('Ext.panel.Panel', {
                renderTo: Ext.getDom('function_1008'),
                //maxWidth: document.documentElement.clientWidth,
                //maxHeight: document.documentElement.clientHeight,
                width: '100%',
                height: document.documentElement.clientHeight,
                layout: 'border',
                items: [roleGrid, functionGrid]
            });

            function buttonEventDispatcher(buttonId) {
                var roleStore = Ext.getStore('dataGridStore_10013_1');
                var selectedRoleId;
                switch (buttonId) {
                    case 23: // refresh Role
                        roleStore.load();
                        break;
                    case 24: // Create Role
                        createRoleWindow.show();
                        break;
                    case 25: // Delete Role
                        selectedRoleId = roleGrid.getSelectionModel().getSelection()[0].get('id');
                        if (selectedRoleId) {
                            Ext.Ajax.request({
                                url: '/base/authority/deleteRole',
                                params: {roleId: selectedRoleId},
                                success: function (response, opts) {
                                    roleStore.load();
                                },
                                failure: function (response, opts) {
                                    Ext.Msg.alert('Failure', 'Delete role failure.');
                                }
                            });
                        }
                        break;
                    case 26: // Add Authority
                        selectedRoleId = roleGrid.getSelectionModel().getSelection()[0].get('id')
                        if (selectedRoleId) {
                            addAuthorityStore.getProxy().extraParams = {roleId: selectedRoleId};
                            addAuthorityStore.load();
                            addAuthorityWindow.show();
                        }
                        break;
                    case 27: // Delete Authority
                        selectedRoleId = roleGrid.getSelectionModel().getSelection()[0].get('id');
                        var selectedFunctionId = addAuthorityGrid.getSelectionModel().getSelection()[0].get('id');
                        if (selectedRoleId && selectedFunctionId) {
                            Ext.Ajax.request({
                                url: '/base/authority/deleteAuthority',
                                params: {roleId: selectedRoleId, functionId: selectedFunctionId},
                                success: function (response, opts) {
                                    functionStore.load();
                                },
                                failure: function (response, opts) {
                                    Ext.Msg.alert('Failure', 'Delete role failure.');
                                }
                            });
                        }
                        break;
                }
            }

            function onRoleGridClick(view, record, item, index, e, eOpts) {
                var id = record.get('id');
                if (id) {
                    functionStore.getProxy().extraParams = {roleId: id};
                    functionStore.getProxy().url = '/base/authority/getAuthority';
                    functionStore.load();
                }
            }
        }

        Ext.onReady(function () {
            initPage();
        });
    })();
</script>
</div>
</body>
</html>