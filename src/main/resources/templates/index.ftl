<html>  
  <head>  
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/prettify.min.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/morris.css">
    <script type="text/javascript" src="/easyui/jquery.min.js"></script>  
    <script type="text/javascript" src="/easyui/jquery.easyui.min.js"></script>

    <script type="text/javascript" src="/easyui/raphael.min.js"></script>
    <script type="text/javascript" src="/easyui/morris.js"></script>
    <script type="text/javascript" src="/easyui/prettify.min.js"></script>

  </head>  
    
  <body>

      <div id="newTradeDiv" class="easyui-panel" title="New Trade" style="width:1500px">
            <form id="ff" method="post">
                <table cellpadding="5">
                    <tr>
                        <td>Cusip</td>
                        <td><input class="easyui-textbox" type="text" name="cusipInput" data-options="required:true"></input></td>
                        <td>Rating</td>
                        <td><input  type="text" id="ratingInput" name="rating" disabled="true"></input></td>

                        <td>Coupon</td>
                        <td><input type="text" id="couponInput" name="coupon" disabled="true"></input></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input class="easyui-textbox" type="text" id="priceInput" name="price" data-options="required:true"></input></td>
                        <td>Yield</td>
                        <td><input type="text" id="yieldInput" name="yield" disabled="true"></input></td>
                    </tr>
                    <tr>
                        <td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="displayPredictionTable()">Submit</a></td>
                    </tr>
                </table>

            </form>
      </div>

          <div id="predicationDiv" class="easyui-panel" title="Predication" style="collapsible:true;">
          <div title="TRADES" style="overflow:auto;padding:10px;display:flex">
              <table id="predicationTable" style="width:700px;height:200px"  toolbar="#toolbar" pagination="false" idField="id" fitColumns="true" singleSelect="true"  >
                  <thead>
                      <tr>
                          <th field="gfcId" width="100">GFC Id</th>
                          <th field="clientName" width="100">Client Name</th>
                          <th field="percentage" width="100">Percentage</th>
                      </tr>
                  </thead>
              </table>
              <div id="graph" style="width:800px;height:200px"></div>
          </div>

      </div>

  <div class="easyui-accordion" style="width:1500px;height:1000px;">
        <div title="TRADES" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;display:flex">
           	<table id="tradeTable" style="width:1000px;height:1000px"  toolbar="#toolbar" pagination="false" idField="id" fitColumns="true" singleSelect="true" >
                <thead>
                     <tr>
                        <th field="tradeId" width="100">Trade Id</th>
                       	 <th field="cusip" width="50">Cusip</th>
                       	 <th field="cusipRating"  width="50" >Cusip Rating</th>
                       	 <th field="cpnRate"  width="50">Coupon</th>
                        <th field="price"  width="50">Price</th>
                         <th field="yield"  width="100" sortable="true" >Yield</th>
                        <th field="createDate"  formatter="formatDatebox" width="50">Create Date</th>

                     </tr>
                </thead>
            </table>
            <div id="tradeDiv" style="visibility:hidden">
                <table id="tradeBids" style="width:500px;height:1000px"  toolbar="#toolbar" pagination="false" idField="id" fitColumns="true" singleSelect="true"  >
                    <thead>
                        <tr>
                            <th field="clientName" width="100">Client Name</th>
                            <th field="bidPrice" width="100">Bid Price</th>
                        </tr>
                    </thead>
                </table>
                <div id="graph"></div>
            </div>

        </div>

        <div title="CUSIPS" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
			<table id="cusipTable" style="width:1500px;height:1000px"  toolbar="#toolbar" pagination="false" idField="id" fitColumns="true" singleSelect="true">

            		<thead>
            			<tr>
            			    <th field="cusip" width="50">Cusip</th>
            				<th field="issShrtName" width="100">IssShrtName</th>
            				<th field="cpnRate"  width="50">Coupon</th>
            				<th field="matD"  width="50">Maturity Date</th>
            				<th field="cpnEffD"  width="50">Cpn Eff Date</th>
            				<th field="frstCpnD"  width="50">Frst Cpn Date</th>
            				<th field="prodTyp"  width="50">Type</th>
            				<th field="ratingAgency"  width="50">Rating Agency</th>
            				<th field="ratingValue"  width="50">Rating Value</th>
            			</tr>
            		</thead>

            	</table>
        </div>

        <div title="BIDS" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
         			<table id="bidTable" style="width:1000px;height:1000px"  toolbar="#toolbar" pagination="false" idField="id" fitColumns="true" singleSelect="true">

                     		<thead>
                     			<tr>
                     			    <th field="gfcId" width="50">GFC Id</th>
                     			    <th field="clientName" width="50">Client Name</th>
                     				<th field="tradeId" width="100">Trade Id</th>
                     				<th field="bidPrice"  width="50">Bid Price</th>
                     				<th field="amount"  width="50">Amount</th>
                     				<th field="tradeYield"  width="50">Trade Yield</th>

                     			</tr>
                     		</thead>

                     	</table>
        </div>
  </div>
  </body>


<script type="text/javascript">

		$(function(){
            init();
			$('#cusipTable').datagrid({
				url:'listCusips',
				saveUrl:'save',
				updateUrl:'update',
				destroyUrl:'delete',
			});
			$('#tradeTable').datagrid({
            	url:'listTrades',
                onSelect:function(index, row) {
                     $('#tradeDiv').css("visibility","visible");
                     $('#tradeBids').datagrid({
                        method : 'get',
                        url:'findBids',
                        queryParams: {
                            tradeId : row.tradeId
                        }
                     });
                     $('#tradeBids').datagrid('reload');
                },
             }

            );
            $('#bidTable').datagrid({
                url:'listBids',
             });

            //$( '#_easyui_textbox_input1').keydown(queryCusip);
            $( '#_easyui_textbox_input1').focusout(queryCusip);
            $( '#_easyui_textbox_input2').keyup(calYield);
		});

        function init() {
            //reset layouts to prevent refresh page causing legacy values
            $('newTradeDiv').css("width", "1500px");
            $('#predicationDiv').panel({collapsible:true,closable:true});
            $('#predicationDiv').panel('close');
        }

        function getElementByXPath(path) {
            return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE,null).singleNodeValue;
        }

        function queryCusip(e) {
            var url = 'findCusip?cusip=' + $( '#_easyui_textbox_input1').val();
            $.getJSON(url,null, function(data) {
                $('#couponInput').val(data.cpnRate);
                $('#ratingInput').val(data.ratingValue);
            })
        }

        function calYield(e) {
            var cusip = $( '#_easyui_textbox_input1').val();
            var price = $( '#_easyui_textbox_input2').val() ;
            var cpn = $('#couponInput').val();
            //alert(price + "," + $( '#priceInput').val() + "," + $( '[name=price]').val()  );
            $('#yieldInput').val(cpn/price*100 + '%');
            //predict(cusip, price);
        }

        function predict() {
            var cusip = $( '#_easyui_textbox_input1').val();
            var price = $( '#_easyui_textbox_input2').val() ;
            var url = 'calWithNeuralNet?cusip=' + cusip+"&price="+price;
            $.getJSON(url,null, function(data) {

            })
        }

        function displayPredictionTable() {
             var cusip = $( '#_easyui_textbox_input1').val();
             var price = $( '#_easyui_textbox_input2').val() ;
             var url = 'calWithNeuralNet';
             $('newTradeDiv').css("width", "1000px");
             $('#predicationDiv').panel('open');

             $('#predicationTable').datagrid({
                url:url,
                queryParams: {
                    cusip : cusip,
                    price : price
                }
             });
             $.getJSON(url + '?cusip='+cusip+'&price='+price,function(jsonData){
                Morris.Bar({
                      element: 'graph',
                      data: jsonData,
                      xkey: ['clientName'],
                      ykeys: ['percentage'],
                      labels: [''],
                      formatter: function(value, data) {return value * 100 ;},
                      yLabelFormat: function(value, data) {return value * 100 ;}
                    }).on('click', function(i, row){
                      console.log(i, row);
                    });
             });


        }

		function formatDatebox(value) {
			if (value == null || value == '') {
				return '';
			}
			var date;
			if (value instanceof Date) {
				date = value;

			} else {
				date = new Date(value);
			}
			var year = date.getFullYear().toString();
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
			return year+"-"+month+"-"+day;
		};

</script>

</html> 