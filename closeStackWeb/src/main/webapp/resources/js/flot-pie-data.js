//$document.ready 는 웹페이지 로딩이 완료되면 실행하도록 하는 함수
$(document).ready(function() {
    console.log("document ready");
    var offset = 0;
    plot();

    function plot() {
    	
   
    	  var data = [{
    	        label: "Series 0",
    	        data: 1
    	    }, {
    	        label: "Series 1",
    	        data: 3
    	    }, {
    	        label: "Series 2",
    	        data: 9
    	    }, {
    	        label: "Series 3",
    	        data: 20
    	    }];

    	    var plotObj = $.plot($("#flot-pie-chart"), data, {
    	        series: {
    	            pie: {
    	                show: true
    	            }
    	        },
    	        grid: {
    	            hoverable: true
    	        },
    	        tooltip: true,
    	        tooltipOpts: {
    	            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
    	            shifts: {
    	                x: 20,
    	                y: 0
    	            },
    	            defaultTheme: false
    	        }
    	    });
    }
});
