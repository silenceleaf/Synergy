<!DOCTYPE html>
<html>
<head>
    <meta name="description" content="User Management"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <link rel="shortcut icon" href="/images/NU-Logo_16*16.png" type="image"/>
    <!--<link rel="stylesheet" type="text/css" href="/extjs5/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css"/>-->
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <!--<script type="text/javascript" src="/extjs5/ext-all-debug.js"></script>-->
    <script type="text/javascript" src="/extjs/ext-all-dev.js"></script>
    <!--<script type="text/javascript" src="/extjs5/packages/ext-locale/build/ext-locale-en.js"></script>-->
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/views/util.js"></script>
    <title>User Management</title>
</head>
<body>
<div id='function_1003'>
    <script type="text/javascript">
        (function () {
            function initPage() {
                var templateStore = createTemplateStore('10002_10013');
                templateStore.load();
                templateStore.on ('load', function () {
                    initComponent();
                });
            }

            function initComponent () {
                var templateStore = Ext.getStore('templateStore_10002_10013');

                var userGrid = Ext.create('Ext.grid.Panel', {
                    title: 'User Grid',
                    id: 'dataGrid_1003_1',
                    store: createDataStore('10002_1'),
                    autoScroll: true,
                    overflowY: 'auto',
                    region: 'west',
                    width: '30%',
                    margin: '0 0 5 0',
                    split: true,
                    columns: [{
                        text: templateStore.getById(347).get('text'),
                        dataIndex: templateStore.getById(347).get('name'),
                        flex: 1
                    }, {
                        text: templateStore.getById(345).get('text'),
                        dataIndex: templateStore.getById(345).get('name'),
                        flex: 2
                    }],
                    listeners: {itemclick: onUserGridClick}
                    //autoLoad: true
                });

                userGrid.getStore().on({
                    add: onGridModified, update: onGridModified, remove: onGridModified
                })

                function onGridModified (store, records) {
                    store.sync();
                }

                var testPanel = Ext.create('Ext.form.Panel', {
                    html: '<h1>test</h1>'
                });

                var userStatusComboBoxStore = Ext.create('Ext.data.Store', {
                    fields: ['value', 'text'],
                    data : [
                        {"value":"1", "text":"Normal"},
                        {"value":"2", "text":"Disabled"},
                        {"value":"3", "text":"Limited"}
                    ]
                });

                var userInfoForm = Ext.create('Ext.form.Panel', {
                    id: 'userInfoForm_1003',
                    frame: false,
                    border: false,
                    title: 'User Information Form',
                    header: false,
                    bodyPadding: '15 35 0 35',
                    collapsible: true,
                    width: 600,
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 150,
                        height: 30
                    },
                    defaults: {
                        anchor: '100%',
                        padding: '10 20 10 20'
                    },
                    items: [{
                        xtype: 'fieldset',
                        title: 'Login Information',
                        defaultType: 'textfield',
                        layout: 'anchor',
                        defaults: {
                            anchor: '100%'
                        },
                        items :[{
                            xtype: 'hidden',
                            name: 'id'
                        }, {
                            fieldLabel: 'User Name',
                            name: 'userName',
                            allowBlank:false
                        }, {
                            fieldLabel: 'Password',
                            name: 'password',
                            inputType: 'password'
                        }, {
                            xtype: 'combobox',
                            fieldLabel: 'Status',
                            store: userStatusComboBoxStore,
                            queryMode: 'local',
                            displayField: 'text',
                            valueField: 'value',
                            name: 'status'
                        }]
                    }, {
                        xtype: 'fieldset',
                        title: 'Contact Information',
                        collapsible: true,
                        collapsed: false,
                        defaultType: 'textfield',
                        layout: 'anchor',
                        defaults: {
                            anchor: '100%'
                        },
                        items: [{
                            fieldLabel: 'Home',
                            name: 'home'
                        }, {
                            fieldLabel: 'Business',
                            name: 'business'
                        }, {
                            fieldLabel: 'Mobile',
                            name: 'mobile'
                        }, {
                            fieldLabel: 'Fax',
                            name: 'fax'
                        }]
                    }]
//                    buttons: [{
//                        text: 'Save'
//                    }, {
//                        text: 'Cancel'
//                    }]
                });

                var userInfoPanel = Ext.create('Ext.panel.Panel', {
                    title: 'User Information',
                    id: 'userInfoPanel_1003',
                    split: true,
                    region: 'center',
                    layout: 'vbox',
                    items: [createToolBar('userInfoPanel_1003', 1, buttonEventDispatcher), userInfoForm]
                });

                var roleGridStore = Ext.create('Ext.data.Store', {
                    fields: ['id', 'name'],
                    proxy: {
                        url: '/base/user/userRoleList',
                        method: 'GET',
                        type: 'ajax',
                        limitParam: undefined,
                        pageParam: undefined,
                        startParam: undefined,
                        reader: {
                            type: 'json',
                            root: 'msg',
                            successProperty: 'success'
                        }
                    }
                });

                var roleGrid = Ext.create('Ext.grid.Panel', {
                    title: 'Role Grid',
                    id: 'dataGrid_1003_2',
                    store: roleGridStore,
                    autoScroll: true,
                    region: 'south',
                    margin: '0 0 5 0',
                    height: 300,
                    split: true,
                    columns: [{
                        text: templateStore.getById(435).get('text'),
                        dataIndex: templateStore.getById(435).get('name'),
                        flex: 1
                    }, {
                        text: templateStore.getById(436).get('text'),
                        dataIndex: templateStore.getById(436).get('name'),
                        flex: 2
                    }],
                    dockedItems: [createToolBar('dataGrid_1003_2', 8, buttonEventDispatcher)],
                    listeners: {itemclick: onRoleGridClick}
                });

                var rightPanel = Ext.create('Ext.panel.Panel', {
                    id: 'rightPanel_1003',
                    split: true,
                    region: 'center',
                    layout: 'border',
                    items:[userInfoPanel, roleGrid]
                });

                var roleSelectStore = Ext.create('Ext.data.Store', {
                    fields: ['id', 'name'],
                    proxy: {
                        url: '/base/user/availableRoleList',
                        method: 'GET',
                        type: 'ajax',
                        limitParam: undefined,
                        pageParam: undefined,
                        startParam: undefined,
                        reader: {
                            type: 'json',
                            root: 'msg',
                            successProperty: 'success'
                        }
                    }
                });

                var roleSelectGrid = Ext.create('Ext.grid.Panel', {
                    title: 'Role Select',
                    id: 'dataGrid_1003_3',
                    store: roleSelectStore,
                    autoScroll: true,
                    overflowY: 'auto',
                    margin: '0 0 5 0',
                    split: true,
                    singleSelect: false,
                    columns: [{
                        text: 'Role Id',
                        dataIndex: 'id',
                        flex: 1
                    }, {
                        singleSelect: false,
                        text: 'Role Name',
                        dataIndex: 'name',
                        flex: 2
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
                                        var selectRolePopupWindow = Ext.getCmp('selectRolePopupWindow_1003');
                                        selectRolePopupWindow.hide();
                                    }
                                }
                            }, {
                                xtype: 'button',
                                text: 'OK',
                                listeners: {
                                    click: function() {
                                        var roleSelectGrid = Ext.getCmp('dataGrid_1003_3');
                                        var selectedRole = roleSelectGrid.getSelectionModel().getSelection()[0];
                                        var userGrid = Ext.getCmp('dataGrid_1003_1');
                                        var selectedUser = userGrid.getSelectionModel().getSelection()[0];
                                        Ext.Ajax.request({
                                            url: '/base/user/addRole',
                                            params: {userId: selectedUser.get('id'), roleId: selectedRole.get('id')},
                                            success: function(response, opts) {
                                                var selectRolePopupWindow = Ext.getCmp('selectRolePopupWindow_1003');
                                                selectRolePopupWindow.hide();
                                                roleGridStore.getProxy().extraParams = {userId: selectedUser.get('id')};
                                                roleGridStore.load();
                                            },
                                            failure: function(response, opts) {
                                                Ext.Msg.alert('Failure', 'Add role failure.');
                                            }
                                        });
                                    }
                                }
                            }
                        ]
                    }]
                    //autoLoad: true
                });

                var roleSelectWindow = Ext.create('Ext.window.Window', {
                    title: 'Select Role',
                    id: 'selectRolePopupWindow_1003',
                    titleAlign: 'center',
                    modal: true,
                    height: 400,
                    width: 600,
                    resizable: false,
                    closable: false,
                    layout: 'fit',
                    items: [roleSelectGrid]
                });

                var mainPanel = Ext.create('Ext.panel.Panel', {
                    renderTo: Ext.getDom('function_1003'),
                    width: '100%',
                    height: document.documentElement.clientHeight,
                    layout: 'border',
                    items: [userGrid, rightPanel]
                });

                function onUserGridClick (view, record, item, index, e, eOpts) {
                    if (record.get('id') > 0) {
                        record.set('password', null);
                        userInfoForm.loadRecord(record);
                        roleGridStore.getProxy().extraParams = {userId: record.get('id')};
                        roleGridStore.load();
                    }
                }

                function onRoleGridClick () {

                }
            }

            Ext.onReady(function () {
                initPage();
            });

            function buttonEventDispatcher(buttonId) {
                var userGrid = Ext.getCmp('dataGrid_1003_1');
                var roleGrid = Ext.getCmp('dataGrid_1003_2');
                var userStore = Ext.getStore('dataGridStore_10002_1');
                var userInfoForm = Ext.getCmp('userInfoForm_1003');
                var defaultFormRecord = Ext.create('DataGridModel_10002_1', {
                    id: '',
                    userName: '',
                    password: '',
                    status: ''
                });
                switch (buttonId) {
                    case 1: // create user
                        userInfoForm.loadRecord(defaultFormRecord);
                        break;
                    case 2: // delete user
                        userStore.remove(userGrid.getSelectionModel().getSelection()[0]);
                        //userStore.sync();
                        break;
                    case 19: // save user
                        var userModel;
                        var userId = userInfoForm.getForm().findField('id').getValue();
                        if (userId == '' || userId == null) { // create new user
                            userModel = Ext.create('DataGridModel_10002_1', {
                                userName: userInfoForm.getForm().findField('userName').getValue(),
                                password: userInfoForm.getForm().findField('password').getValue(),
                                status: userInfoForm.getForm().findField('status').getValue()
                            });
                            userStore.add(userModel);
                        } else { // edit user
                            userModel = userGrid.getSelectionModel().getSelection()[0];
                            userModel.set('userName', userInfoForm.getForm().findField('userName').getValue());
                            userModel.set('password', userInfoForm.getForm().findField('password').getValue());
                            userModel.set('status', userInfoForm.getForm().findField('status').getValue());
                        }
                        //userStore.sync();
                        userInfoForm.loadRecord(defaultFormRecord);
                        break;
                    case 22: // refresh user list
                        userStore.load();
                        userInfoForm.loadRecord(defaultFormRecord);
                        break;
                    case 20: // add role
                        var userIdToAddRole = userGrid.getSelectionModel().getSelection()[0].get('id');
                        if (userIdToAddRole > 0) {
                            var roleSelectGrid = Ext.getCmp('dataGrid_1003_3');
                            roleSelectGrid.getStore().getProxy().extraParams = {userId: userIdToAddRole};
                            roleSelectGrid.getStore().load();
                            var selectRolePopupWindow = Ext.getCmp('selectRolePopupWindow_1003');
                            selectRolePopupWindow.show();
                        } else {
                            Ext.Msg.alert('Failure', 'Please select a user.');
                        }
                        break;

                    case 21: // delete role
                        var deleteUserId = userGrid.getSelectionModel().getSelection()[0].get('id');
                        if (deleteUserId == null || deleteUserId == '') {
                            Ext.Msg.alert('Failure', 'Please select a user.');
                            return;
                        }
                        var deleteRoleId = roleGrid.getSelectionModel().getSelection()[0].get('id');
                        if (deleteRoleId == null || deleteRoleId == '') {
                            Ext.Msg.alert('Failure', 'Please select a role.');
                            return;
                        }
                        Ext.Ajax.request({
                            url: '/base/user/deleteRole',
                            params: {userId: deleteUserId, roleId: deleteRoleId},
                            success: function(response, opts) {
                                roleGrid.getStore().remove(roleGrid.getSelectionModel().getSelection()[0]);
                            },
                            failure: function(response, opts) {
                                Ext.Msg.alert('Failure', 'Delete role failure.');
                            }
                        });

                        break;
                }
            }
        })();
    </script>
</div>
</body>
</html>