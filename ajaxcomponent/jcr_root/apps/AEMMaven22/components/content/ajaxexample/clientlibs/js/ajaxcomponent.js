(function ($) {
  'use strict';
 $("button").click(function(){
    var page = "/bin/stockAPI.json";
    var symbol = $(this).data("symbol");
    var queryString = '?symbol=' + symbol;
    var apiURL = page + queryString;

    $.ajax({
       url: apiURL, 
         success: function(result) {
             var stockSymbol = symbol;
             $("#ajax-example #stock-symbole").html(stockSymbol);
             var longName = result.longName;
             $("#stock-longName").html(longName);
             var currentPrice = result.regularMarketPrice;
             $("#ajax-example #stock-currentPrice").html(currentPrice);
             var high = result.regularMarketDayHigh;
             $("#ajax-example #stock-high").html(high);
             var low = result.regularMarketDayLow;
             $("#ajax-example #stock-low").html(low);
             var close = result.regularMarketPreviousClose;
             $("#ajax-example #stock-close").html(close);
             var time = result.regularMarketTime;
             $("#ajax-example #stock-time").html(time);
       }
     });
  });


})(jQuery);