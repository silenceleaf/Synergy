<!DOCTYPE html>
<html>
<head>
    <meta name="description" content="Metadata Management"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <link rel="shortcut icon" href="/images/NU-Logo_16*16.png" type="image"/>
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <script type="text/javascript" src="/extjs/ext-all-dev.js"></script>
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/views/util.js"></script>
    <title>Metadata Management</title>
</head>
<body>
<div id='function_1007' style="width: 100%; height: 100%">
<script type="text/javascript">
(function () {
    function initPage() {
        var templateStore = createTemplateStore('10005_10008_201');
        templateStore.load();
        templateStore.on ('load', function () {
            initComponent();
        });
    }

    function initComponent () {
        var templateStore = Ext.getStore('templateStore_' + '10005_10008_201');

        var metadataGrid = Ext.create('Ext.grid.Panel', {
            title: 'Metadata Grid',
            id: 'dataGrid_1007_1',
            store: createDataStore('10005_1'),
            autoScroll: true,
            overflowY: 'auto',
            region: 'center',
            width: '30%',
            margin: '0 0 5 0',
            columns: [{
                text: templateStore.getById(368).get('text'),
                dataIndex: templateStore.getById(368).get('name'),
                flex: 1
            }, {
                text: templateStore.getById(370).get('text'),
                dataIndex: templateStore.getById(370).get('name'),
                flex: 3
            }, {
                text: templateStore.getById(375).get('text'),
                dataIndex: templateStore.getById(375).get('name'),
                flex: 1
            }],
            dockedItems: [createToolBar('dataGrid_1007_1', 5, buttonEventDispatcher)],
            listeners: {itemclick: onMetadataGridClick}
        });

        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            pluginId: 'cellPlugin_1007_1',
            clicksToMoveEditor: 1,
            autoCancel: false
        });


        var metadataDetailGrid = Ext.create('Ext.grid.Panel', {
            title: 'Metadata Detail Grid',
            id: 'dataGrid_1007_2',
            store: createDataStore('10008_1'),
            autoScroll: true,
            overflowY: 'auto',
            width: '70%',
            split: true,
            region: 'east',
            margin: '0 0 5 0',
            columns: [{
                text: templateStore.getById(403).get('text'),
                dataIndex: templateStore.getById(403).get('name'),
                flex: 1
            }, {
                text: templateStore.getById(401).get('text'),
                dataIndex: templateStore.getById(401).get('name'),
                flex: 1
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
            }],
            dockedItems: [createToolBar('dataGrid_1007_2', 6, buttonEventDispatcher)],
            plugins: [rowEditing]
        });


        var mainPanel = Ext.create('Ext.panel.Panel', {
            renderTo: Ext.getDom('function_1007'),
            //maxWidth: document.documentElement.clientWidth,
            //maxHeight: document.documentElement.clientHeight,
            width: '100%',
            height: document.documentElement.clientHeight,
            layout: 'border',
            items: [metadataDetailGrid, metadataGrid]
        });

        function buttonEventDispatcher(buttonId) {
            switch (buttonId) {
                case 10: // refresh left list
                    var metadataStore = Ext.getStore('dataGridStore_10005_1');
                    metadataStore.getProxy().extraParams = {type: 2};
                    metadataStore.load();
                    break;
                case 11: // save record
                    var metadataDetailStore = Ext.getStore('dataGridStore_10008_1');
                    metadataDetailStore.getProxy().extraParams = undefined;
                    metadataDetailStore.sync();
                    break;
                case 16: // refresh metadata
                    Ext.Ajax.request({
                        url: '/base/metadata/refresh',
                        success: function(response, opts) {
                            Ext.Msg.alert('Success', 'Update metadata success.');
                        },
                        failure: function(response, opts) {
                            Ext.Msg.alert('Failure', 'Update metadata failure.');
                        }
                    });
                    break;
            }
        }

        function onMetadataGridClick(view, record, item, index, e, eOpts) {
            var id = record.get('id');
            if (id != null) {
                var metadataDetailStore = Ext.getStore('dataGridStore_10008_1');
                metadataDetailStore.getProxy().extraParams = {'templateEntity.pkTemplate': id};
                metadataDetailStore.load();
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