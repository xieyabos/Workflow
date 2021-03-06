    $(document).ready(function(){
    	$("#dept").hide();
	    var grid=$("#grid-data").bootgrid({
	    	navigation:2,
  			columnSelection:false,
		    ajax:true,
		    url:"getHistoryList",
		    formatters: {
		    "processInstanceId": function(column, row){
		    	return row.leaveApply.processInstanceId;
		    },
		    "userId":function(column, row){
		    	return row.leaveApply.userId;
		    },
		    "leaveType":function(column, row){
		    	return row.leaveApply.leaveType;
		    },
		    "applyTime":function(column, row){
		    	return getTime(row.leaveApply.applyTime);
		    },
		    "commands": function(column, row)
		    {
		            return "<button class=\"btn btn-xs btn-default ajax-link command-run1\" data-row-id=\"" + row.leaveApply.processInstanceId + "\">查看详情</button>"+
                        "<button class=\"btn btn-xs btn-default ajax-link command-run2\" data-row-id=\"" + row.leaveApply.processInstanceId + "\">删除记录</button>";
		    }
	    	}
	    
	    }).on("loaded.rs.jquery.bootgrid", function()
	    		{
	    	    grid.find(".command-run1").on("click", function(e)
	    	    {
	    	    	$("#processinfo").modal();
	    	    	var processInstanceId=$(this).data("row-id");
	    	    	$("#activity").html("<tr><th>活动名称</th><th>活动类型</th><th>办理人</th><th>活动开始时间</th><th>活动结束时间</th></tr>");
                    $.ajax({
                        url:"processinfo/"+processInstanceId,
                        type:"post",
                        contentType:"application/json;charset=UTF-8",
                        dataType:"json",
                        success : function(data){
                            for(var a=0;a<data.length;a++)
                                $("#activity").append("<tr><td>"+data[a].activityName+"</td><td>"+data[a].activityType+"</td><td>"+data[a].assignee+"</td>" +
                                    "<td>"+getTime(data[a].startTime)+"</td><td>"+getTime(data[a].endTime)+"</td>");
                        }
                    })
	    	    });
				grid.find(".command-run2").on("click", function(e)
				{
                    var processInstanceId=$(this).data("row-id");
                    $(".btn").attr("disabled","disabled");
					$.ajax({
						url:"removeProcess/"+processInstanceId,
						type:"get",
						success : function(data){
							if(data.code == 0){
								alert("处理成功");
								$(".btn").removeAttr("disabled");
								LoadAjaxContent("myhistory");
							}else{
								alert(data.msg);
							}
						}
					})
				});
	    });
	  });