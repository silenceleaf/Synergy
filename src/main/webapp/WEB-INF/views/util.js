/**
 * Created by junyan Zhang on 14-1-29.
 */
Ext.define('TemplateModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'defaultValue',  type: 'string'},
        {name: 'text', type: 'string'}
    ]
});

function createTemplateStore (templateId) {
    var templateStore = Ext.create ('Ext.data.Store' ,{
        storeId: 'templateStore_' + templateId,
        model: 'TemplateModel',
        proxy: {
            type: 'ajax',
            url: '/template',
            limitParam : undefined,
            pageParam : undefined,
            startParam : undefined,
            extraParams : {id: templateId},
            reader: {
                type: 'json',
                root: 'msg'
            }
        }
    });
    return templateStore;
}
// -------------------------------------------
Ext.define('PromptModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'message',  type: 'string'}
    ]
});

function getPromptMsg (promptBody) {
    var promptStore = Ext.create ('Ext.data.Store', {
        storeId: 'promptStore',
        model: 'PromptModel',
        data: promptBody,
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                root: 'root'
            }
        }
    });
    var promptMsg = '<div style="color:red">';
    promptStore.each (function (record) {
        promptMsg += record.get('message');
        promptMsg += '<br />'
    })
    promptMsg += '</div>'
    return promptMsg;
}
// -------------------------------------------------
Ext.define('ToolBarModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'xtype',  type: 'string'},
        {name: 'text',  type: 'string'},
        {name: 'width',  type: 'int'},
        {name: 'toolTip', type: 'string'}
    ]
});

function createToolBar (renderTo, toolBarId, actionHandler) {
    var toolBar = Ext.create ('Ext.toolbar.Toolbar', {
        // generic toolBar setting
        id: 'toolBar_' + toolBarId,
        width: '100%',
        renderTo: Ext.getDom(renderTo)
        //renderTo: Ext.getDom('function_' + functionId)
    });

    var toolBarStore = Ext.create ('Ext.data.Store', {
        storeId: 'toolBarStore_' + toolBarId,
        model: 'ToolBarModel',
        proxy: {
            method: 'GET',
            type: 'ajax',
            limitParam : undefined,
            pageParam : undefined,
            startParam : undefined,
            extraParams: { toolBarId: toolBarId },
            url: '/toolBar',
            reader: {
                type: 'json',
                root: 'msg'
            }
        },
        autoLoad: true,
        listeners: {
            load: function (store, records, success, operation, opts) {
                // try get toolBar directly
                var toolbar = Ext.getCmp('toolBar_' + toolBarId);
                store.each (function (record) {
                    var component;
                    switch (record.data.xtype) {
                        case 'button':
                            component = Ext.create ('Ext.button.Button', {
                                // generic toolBar setting
                                id: 'button_' + record.data.id,
                                xtype: record.data.xtype,
                                text: record.data.text,
                                width: record.data.width,
                                height: 30,
                                tooltip: record.data.tooltip,
                                listeners: {
                                    click: function(){actionHandler(record.data.id);}
                                }
                            });
                            break;
                        case 'tbfill':
                            component = Ext.create ('Ext.toolbar.Fill', { });
                            break;
                        case 'menu':
                            // something to do
                            break;
                        default:
                            component = Ext.create ('Ext.toolbar.Separator', { });
                            break;
                    }
                    toolbar.add(component);
                });
            }
        }
    });
    return toolBar;
}

function addNewTab(functionId, title) {
    var tabPanel = Ext.getCmp('mainTabPanel');
    if (functionId == '' || functionId == undefined || functionId == null) {
        var tabCard = Ext.getCmp('homeTab');
    } else {
        var tabCard = Ext.getCmp('tab' + functionId);
        if (tabCard == undefined) {
            var tabCard = Ext.create('Ext.panel.Panel', {
                id: 'tab' + functionId,
                title: title,
                autoScroll: true,
                closable: true,
                layout: {
                    type: 'vbox',
                    align: 'center',
                    pack: 'start'
                },
                border: false,
                loader: {
                    ajaxOptions: {method: 'GET', timeout: 5000},
                    autoLoad: true,
                    url: '/function',
                    scripts: true,
                    loadMask: {msg: 'loading'},
                    params : {functionId: functionId},
                    renderer: 'html',
                    target: 'tab' + functionId
                }
            });
            tabPanel.add(tabCard);
        }
    }
    tabPanel.setActiveTab(tabCard);
}
// --------------------------------------------------

function createDataStore (templateId) {
    var dataGridModelStore = Ext.create ('Ext.data.Store', {
        storeId: 'dataGridModelStore_' + templateId,
        model: 'TemplateModel',
        autoDestroy: true,
        proxy: {
            method: 'GET',
            type: 'ajax',
            limitParam: undefined,
            pageParam: undefined,
            startParam: undefined,
            extraParams: { id: templateId },
            url: '/metadata',
            reader: {
                type: 'json',
                root: 'msg',
                successProperty: 'success'
            }
        },
        listeners: {
            load : function (store, records, success, operation, opts) {
                if (success == true) {
                    var fieldsArray = [];
                    store.each(function (record){
                        var field = {name: record.get('name'), type: record.get('type'), defaultValue: record.get('defaultValue')};
                        fieldsArray.push(field);
                    });
                    var dataGridModel = Ext.ModelManager.getModel('DataGridModel_' + templateId);
                    dataGridModel.setFields(fieldsArray);
                } else {
                    console.log("load dataGridModelStore failure. metadataId = " + templateId);
                }
            }
        },
        autoLoad: true
    });

    Ext.define('DataGridModel_' + templateId, {
        extend: 'Ext.data.Model'
    });

    var dataGridStore = Ext.create ('Ext.data.Store', {
        storeId: 'dataGridStore_' + templateId,
        model: 'DataGridModel_' + templateId,
        autoDestroy: true,
        proxy: {
            method: 'GET',
            type: 'ajax',
            batchActions: true,
            //extraParams: extraParameters,
            limitParam: undefined,
            pageParam: undefined,
            startParam: undefined,
            api: {
                read    : '/data/read/' + templateId,
                create  : '/data/create/' + templateId,
                update  : '/data/update/' + templateId,
                destroy : '/data/destroy/' + templateId
            },
            reader: {
                type: 'json',
                root: 'msg',
                successProperty: 'success'
            },
            writer: {
                type: 'json',
                writeAllFields: false
                //root: 'data'
            }
        },
        autoLoad: false
    });

    return dataGridStore;
}

//function readData (templateId, queryParameters) {
//    var store = Ext.getStore('dataGridStore_' + templateId);
//    if (store == undefined) {
//        console.log("Store " + storeId + " not exists.");
//        return null;
//    }
//    queryParameters.templateId = templateId;
//    store.proxy.extraParams = queryParameters;
//    store.load();
//    return store;
//}
//
//function readData (templateId) {
//    var store = Ext.getStore('dataGridStore_' + templateId);
//    if (store == undefined) {
//        console.log("Store " + storeId + " not exists.");
//        return null;
//    }
//    var queryParameters = {templateId: templateId};
//    store.proxy.extraParams = queryParameters;
//    store.load();
//    return store;
//}