//$document.ready 는 웹페이지 로딩이 완료되면 실행하도록 하는 함수
$(document).ready(function() {
	console.log("document ready");
	var offset = 0;
	plot();
	plot2();
	plot3();
	plot4();

	function plot() {

		var data = [ {
			label : "using Instance",
			data : 6
		}, {
			label : "total Instance",
			data : 10
		} ];

		var plotObj = $.plot($("#flot-pie-chart"), data, {
			series : {
				pie : {
					show : true,
					radius : 1,
					label : {
						show : false,
					},
					
				}
			},
			grid : {
				hoverable : true
			},
			tooltip : true,
			tooltipOpts : {
				content : "%p.0%, %s", // show percentages, rounding to 2
				// decimal places
				shifts : {
					x : 20,
					y : 0
				},
				defaultTheme : false
			},
			legend : {
				show : false
			}
		});
	}

	function plot2() {

		var data = [ {
			label : "using VCPUs",
			data : 13
		}, {
			label : "total VCPUs",
			data : 20
		} ];

		var plotObj = $.plot($("#flot-pie-chart2"), data, {
			series : {
				pie : {
					show : true,
					radius : 1,
					label : {
						show : false
					},
				}
			},
			grid : {
				hoverable : true
			},
			tooltip : true,
			tooltipOpts : {
				content : "%p.0%, %s", // show percentages, rounding to 2
				// decimal places
				shifts : {
					x : 20,
					y : 0
				},
				defaultTheme : false
			},
			legend : {
				show : false
			}
		});
	}

	function plot3() {

		var data = [ {
			label : "using RAM(GB)",
			data : 20
		}, {
			label : "total RAM(GB)",
			data : 50
		} ];

		var plotObj = $.plot($("#flot-pie-chart3"), data, {
			series : {
				pie : {
					show : true,
					radius : 1,
					label : {
						show : false
					}
				}
			},
			grid : {
				hoverable : true
			},
			tooltip : true,
			tooltipOpts : {
				content : "%p.0%, %s", // show percentages, rounding to 2
				// decimal places
				shifts : {
					x : 20,
					y : 0
				},
				defaultTheme : false
			},
			legend : {
				show : false
			}
		});
	}

	function plot4() {

		var data = [ {
			label : "using IP",
			data : 6
		}, {
			label : "total IP",
			data : 50
		} ];

		var plotObj = $.plot($("#flot-pie-chart4"), data, {
			series : {
				pie : {
					show : true,
					radius : 1,
					label : {
						show : false
					}
				}
			},
			grid : {
				hoverable : true
			},
			tooltip : true,
			tooltipOpts : {
				content : "%p.0%, %s", // show percentages, rounding to 2
				// decimal places
				shifts : {
					x : 20,
					y : 0
				},
				defaultTheme : false
			},
			legend : {
				show : false
			}
		});
	}
});
