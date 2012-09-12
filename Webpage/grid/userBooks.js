jQuery("#list2").jqGrid({
   	url:'server.php?q=2',
	datatype: "json",
   	colNames:['Cover','Title','Authors', 'Published', 'Pages','Insertion Date','Language','ISBN'],
   	colModel:[
   		{name:'imgURL',index:'imgURL', width:55},
   		{name:'title',index:'title', width:90},
   		{name:'authors',index:'authors', width:100},
   		{name:'publishedYear',index:'publishedYear', width:80, align:"right"},
   		{name:'pageCount',index:'pageCount', width:80, align:"right"},		
   		{name:'dateOfInsert',index:'dateOfInsert', width:80,align:"right"},		
   		{name:'language',index:'language', width:150, sortable:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pager2',
   	sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    caption:"JSON Example"
});
jQuery("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});
