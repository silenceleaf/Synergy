<!DOCTYPE html>
<html>
<head>
    <meta name="description" content="Function Management"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <!--<link rel="stylesheet" type="text/css" href="/extjs5/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css"/>-->
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <!--<script type="text/javascript" src="/extjs5/ext-all-debug.js"></script>-->
    <script type="text/javascript" src="/extjs/ext-all-dev.js"></script>
    <!--<script type="text/javascript" src="/extjs5/packages/ext-locale/build/ext-locale-en.js"></script>-->
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/views/util.js"></script>
    <title>Function Management</title>
</head>
<body>
<div id='function_1006' style="width: 100%; height: 100%">
    <script type="text/javascript">
        (function () {
            function initPage() {
                var templateStore = createTemplateStore('10007_10008_201');
                templateStore.load();
                templateStore.on ('load', function () {
                    initComponent();
                });
            }

            function initComponent () {
                Ext.define('FuncTreeModel_1006', {
                    extend: 'Ext.data.Model',
                    fields: [
                        {name: 'id', type: 'int'},
                        {name: 'text', type: 'string'},
                        {name: 'url', type: 'string'},
                        {name: 'leaf', type: 'boolean'}
                    ]
                });

                var funcTreeStore = Ext.create('Ext.data.TreeStore', {
                    id: "funcTreeStore_1006",
                    model: 'FuncTreeModel_1006',
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
                    id: "funcTreePanel_1006",
                    region: 'west',
                    useArrows: true,
                    collapsible: true,
                    title: 'Function',
                    width: 300,
                    minSize: 100,
                    maxSize: 500,
                    split: true,
                    rootVisible: true,
                    store: funcTreeStore,
                    margin: '0 0 5 0',
                    listeners: {itemclick: onFuncTreeClick}
                });

                var templateRowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
                    pluginId: 'cellPlugin_1006_1',
                    clicksToMoveEditor: 1,
                    autoCancel: false
                });

                var templateStore = Ext.getStore('templateStore_' + '10007_10008_201');
                var templateFieldGrid = Ext.create('Ext.grid.Panel', {
                    title: 'Template Field Grid',
                    id: 'dataGrid_1006_1',
                    store: createDataStore('10008_2'),
                    columns: [{
                            text: templateStore.getById(403).get('text'),
                            dataIndex: templateStore.getById(403).get('name'),
                            flex: 1
                        }, {
                            text: templateStore.getById(401).get('text'),
                            dataIndex: templateStore.getById(401).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(408).get('text'),
                            dataIndex: templateStore.getById(408).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(400).get('text'),
                            dataIndex: templateStore.getById(400).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(399).get('text'),
                            dataIndex: templateStore.getById(399).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(402).get('text'),
                            dataIndex: templateStore.getById(402).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(406).get('text'),
                            dataIndex: templateStore.getById(406).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(407).get('text'),
                            dataIndex: templateStore.getById(407).get('name'),
                            flex: 1,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: true
                            }
                        }, {
                            text: templateStore.getById(449).get('text'),
                            dataIndex: templateStore.getById(449).get('name'),
                            flex: 1
                        }],
                    dockedItems: [createToolBar('dataGrid_1006_1', 4, buttonEventDispatcher)],
                    plugins: [templateRowEditing]
                });

                var firstTabPanel = Ext.create ('Ext.panel.Panel', {
                    id: 'firstTabPanel_1006',
                    title: 'Template',
                    layout: 'fit',
                    width: '100%',
                    items: [templateFieldGrid]
                });

                var operationRowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
                    pluginId: 'cellPlugin_1006_2',
                    clicksToMoveEditor: 1,
                    autoCancel: false
                });

                var operationGrid = Ext.create('Ext.grid.Panel', {
                    title: 'Operation Grid',
                    id: 'dataGrid_1006_2',
                    store: createDataStore('10007_1'),
//                    autoScroll: true,
//                    overflowY: 'auto',
//                    width: '100%',
                    columns: [{
                        text: templateStore.getById(397).get('text'),
                        dataIndex: templateStore.getById(397).get('name'),
                        flex: 1
                    }, {
                        text: templateStore.getById(393).get('text'),
                        dataIndex: templateStore.getById(393).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(391).get('text'),
                        dataIndex: templateStore.getById(391).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(394).get('text'),
                        dataIndex: templateStore.getById(394).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(392).get('text'),
                        dataIndex: templateStore.getById(392).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(395).get('text'),
                        dataIndex: templateStore.getById(395).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(396).get('text'),
                        dataIndex: templateStore.getById(396).get('name'),
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: templateStore.getById(452).get('text'),
                        dataIndex: templateStore.getById(452).get('name'),
                        flex: 1
                    }],
                    dockedItems: [createToolBar('dataGrid_1006_2', 7, buttonEventDispatcher)],
                    plugins: [operationRowEditing]
                });

                var secondTabPanel = Ext.create ('Ext.panel.Panel', {
                    id: 'secondTabPanel_1006',
                    title: 'Operation',
                    layout: 'fit',
                    width: '100%',
                    items: [operationGrid]
                });

                var tabPanel = Ext.create('Ext.tab.Panel', {
                    id: "tabPanel_1006",
                    region: 'center',
                    layout: 'fit',
                    activeTab: 0,
                    minTabWidth: 200,
                    //deferredRender: false,
                    items: [firstTabPanel, secondTabPanel],
                    collapsible: false,
                    margin: '0 0 5 0'
                });

                var mainPanel = Ext.create('Ext.panel.Panel', {
                    renderTo: Ext.getDom('function_1006'),
                    width: '100%',
                    height: document.documentElement.clientHeight,
                    layout: 'border',
                    items: [funcTreePanel, tabPanel]
                });

                function buttonEventDispatcher(buttonId) {
                    var templateFieldStore = templateFieldGrid.getStore();
                    var operationStore = operationGrid.getStore();
                    var funcTreePanel = Ext.getCmp('funcTreePanel_1006');
                    var selectedNode = funcTreePanel.getSelectionModel().getSelection()[0];
                    switch (buttonId) {
                        case 7: // add template record
                            templateRowEditing.cancelEdit();
                            if (selectedNode && selectedNode.get('leaf')) {
                                var newTemplateFieldRow = Ext.create('DataGridModel_10008_2', {fkFunction: selectedNode.get('id')});
                                templateFieldStore.insert(0, newTemplateFieldRow);
                                templateRowEditing.startEdit(0, 0);
                            } else {
                                Ext.Msg.alert('Failure', 'Please select a tree node.');
                            }
                            break;
                        case 8: // delete template record
                                var selectedTemplateFieldRow = templateFieldGrid.getSelectionModel().getSelection()[0];
                                if (selectedTemplateFieldRow && selectedTemplateFieldRow.get('id') > 0) {
                                    templateFieldStore.remove(selectedTemplateFieldRow);
                                } else {
                                    Ext.Msg.alert('Failure', 'Please select a row to delete.');
                                }
                            break;
                        case 9: // save record
                                templateFieldStore.getProxy().extraParams = undefined;
                                templateFieldStore.sync();
                            break;
                        case 12: // add button
                                operationRowEditing.cancelEdit();
                                if (selectedNode && selectedNode.get('leaf')) {
                                    var newOperationRow = Ext.create('DataGridModel_10007_1', {fkFunction: selectedNode.get('id')});
                                    operationStore.insert(0, newOperationRow);
                                    templateRowEditing.startEdit(0, 0);
                                } else {
                                    Ext.Msg.alert('Failure', 'Please select a tree node.');
                                }
                            break;
                        case 13: // delete button
                                var selectedOperationRow = operationGrid.getSelectionModel().getSelection()[0];
                                if (selectedOperationRow && selectedOperationRow.get('id') > 0) {
                                    operationStore.remove(selectedOperationRow);
                                } else {
                                    Ext.Msg.alert('Failure', 'Please select a row to delete.');
                                }
                            break;
                        case 14:  // save button
                                operationStore.getProxy().extraParams = undefined;
                                operationStore.sync();
                            break;
                        case 17: // refresh cache
                            break;
                    }
                }

                function onFuncTreeClick (view, record, item, index, e) {
                    if (tabPanel.getActiveTab().id == 'firstTabPanel_1006') {
                        if (record.get('leaf') == true) {
                            var templateFieldStore = templateFieldGrid.getStore();
                            templateFieldStore.getProxy().extraParams = {'templateEntity.fkFunction': record.get('id')};
                            templateFieldStore.load();
                        }
                    } else if (tabPanel.getActiveTab().id == 'secondTabPanel_1006') {
                        if (record.get('leaf') == true) {
                            var operationStore = operationGrid.getStore();
                            operationStore.getProxy().extraParams = {'operationGroup.fkFunction': record.get('id')};
                            operationStore.load();
                        }
                    } else {
                        Ext.Msg.alert('Failure', 'No active tab.');
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