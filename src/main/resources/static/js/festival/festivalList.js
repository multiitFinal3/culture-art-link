$(document).ready(
    function(){

        //# region 카드 전체 함수



       /**
       * 카드 클릭 시 상세 페이지로 이동
       *
       */
        $(document).on('click','.card',function(){

            var festivalId = $(this).attr('id');
            window.location.href='/festival/festival-detail?festivalId='+festivalId;

        })



//       /**
//       * 카드 호버 시 찜 관심없음 버튼 보임
//       *
//       */
//        $(document).on('mouseover','.card', function(){
//
//            var btns = $(this).find('button');
//            btns.each(function(){
//
//                var btn = $(this);
//                btn.removeClass('none');
//
//            })
//        })
//
//
//       /**
//       * 카드 호버 아닐 시 찜 관심없음 버튼 안보임
//       *
//       */
//        $(document).on('mouseout','.card', function(){
//
//            var btns = $(this).find('button');
//            btns.each(function(){
//
//                var btn = $(this);
//                btn.addClass('none');
//
//            })
//        })



        //#endRegion


        //#region 키워드 추천 관련 함수

           /**
           * 키워드 추천 함수
           *
           */
            function findKeywordRecommendFestivalList(){

                $('#list2').html("");
                $.ajax({

                    url: '/festival/findKeywordRecommendFestivalList',
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(list){
                        $.each(list, function(index, festival){


                            var indexNew = index + 1;

                            var start = festival.startDate.length >0?
                            festival.startDate.substring(0,10):
                            "없음";

                            var end = festival.endDate.length >0?
                            festival.endDate.substring(0,10):
                            "없음";

                            var firstHtml= ``;

                            if(! festival.imgUrl || festival.imgUrl == "null"){

                                firstHtml = `
                                    <div class="card" id="${festival.festivalId}">
                                        <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap">

                                `;

                            }else{

                                firstHtml = `
                                    <div class="card" id="${festival.festivalId}">
                                        <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap">

                                `;

                            }



                            var secondHtml = `
                                <div class="card-body">
                                      <h5 class="card-title" title="${festival.festivalName}">${festival.festivalName}</h5>
                                      <p class="card-text" title="${festival.festivalContent}">${festival.festivalContent}</p>
                                      <p class="card-text">${start} - ${end}</p>
                                      <p class="card-text t2" style="text-align:center;">★ ${festival.avgRate.toFixed(1)}</p>


                                      <div class="buttonZone">

                                        <button class="whiteHeartBtn">
                                          <img src="https://kr.object.ncloudstorage.com/team3/common/upNo.png"
                                             style="width : 25px; height: 25px;" class="heart" value="${festival.festivalId}">
                                        </button>

                                        <button class="whiteHateBtn">
                                          <img src="https://kr.object.ncloudstorage.com/team3/common/downNo.png"
                                             style="width : 25px; height: 25px;" class="hate" value="${festival.festivalId}">
                                        </button>

                                     </div>

                                </div>
                            `;


                            var finalHtml = firstHtml + secondHtml;

                            $('#list2').append(finalHtml);

                            findLoveList();
                            findHateList();



                        })


                    }

                })
            }


        findKeywordRecommendFestivalList();


        //#end region


        //#region festivalList 관련 함수


        /**
       * DB 전체 리스트 페이지 없이 전부 조회 기능
       *
       */
        function findDBFestivalAllList(){

            $('#list1').html("");
            $('#pageNum1').html("");

            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalAllList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $.each(list, function(index, festival){

                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "없음";

                        var firstHtml= ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap">

                            `;

                        }else{

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap">

                            `;

                        }


                        var secondHtml = `
                            <div class="card-body">
                                  <h5 class="card-title" title="${festival.festivalName}">${festival.festivalName}</h5>
                                  <p class="card-text" title="${festival.festivalContent}">${festival.festivalContent}</p>
                                  <p class="card-text">${start} - ${end}</p>
                                  <p class="card-text t2" style="text-align:center;">★ ${festival.avgRate.toFixed(1)}</p>
                                  <div class="buttonZone">

                                      <button class="whiteHeartBtn">
                                        <img src="https://kr.object.ncloudstorage.com/team3/common/upNo.png"
                                           style="width : 25px; height: 25px;" class="heart" value="${festival.festivalId}">
                                      </button>

                                      <button class="whiteHateBtn">
                                        <img src="https://kr.object.ncloudstorage.com/team3/common/downNo.png"
                                           style="width : 25px; height: 25px;" class="hate" value="${festival.festivalId}">
                                      </button>

                                  </div>
                            </div>

                        </div>
                        `;


                        var finalHtml = firstHtml + secondHtml;

                        $('#list1').append(finalHtml);

                        findLoveList();
                        findHateList();

                    })


                }

            })
        }

        findDBFestivalAllList();
        $('#festivalList').addClass('none');
        $('#festivalRecommendation').removeClass('none');
        $('#allList').closest('.genre-item').removeClass('active');
        $('#keywordList').closest('.genre-item').addClass('active');





        /**
       * DB 전체 리스트 페이지별 조회 기능
       * @param {int} page 페이지
       */
        function findDBFestivalList(page){

            $('#list1').html("");


            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalList?page=' + page,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $.each(list, function(index, festival){

                        var index1 = (index + 1) + (page-1)*5;

                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "없음";




                        var firstHtml= ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap">

                            `;

                        }else{

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap">

                            `;

                        }


                        var secondHtml = `
                            <div class="card-body">
                                  <h5 class="card-title" title="${festival.festivalName}">${festival.festivalName}</h5>
                                  <p class="card-text" title="${festival.festivalContent}">${festival.festivalContent}</p>
                                  <p class="card-text">${start} - ${end}</p>
                                  <p class="card-text t2" style="text-align:center;">★ ${festival.avgRate.toFixed(1)}</p>
                                  <div class="buttonZone">
                                    <button class="btn btn-primary heart" type="button" value="${festival.festivalId}">
                                      <img src="/img/festival/heart.png"
                                           style="width : 100%; height: 100%;">
                                      </button>
                                      <button class="btn btn-primary hate" type="button" value="${festival.festivalId}">
                                            <img src="/img/festival/trash.png"
                                                 style="width : 100%; height: 100%;">
                                      </button>
                                  </div>
                            </div>
                            
                        </div>
                        `;


                        var finalHtml = firstHtml + secondHtml;

                        $('#list1').append(finalHtml);

                        findLoveList();
                        findHateList();

                    })


                }

            })
        }



        /**
       * DB 전체 갯수를 알아와 페이지 버튼 추가 기능
       */
        function findDBFestivalCount(){

            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalCount',
                method: 'POST',
                contentType: 'application/json',
                success: function(count){

                    $('#pageNum1').html("");

                    var page = 0;

                    if(count % 5 ==0){
                        page = count/5;
                    }else{
                        page = count/5 +1;
                    }

                    for(var p=1; p<=page; p++){

                        $('#pageNum1').append(`<button class="pageBtn1">${p}</button>`)

                    }


                }

            })


        }

        // 버튼 붙히기 : 전체를 불러오는 방식으로 변경함
        // findDBFestivalCount();

        /**
       * DB 페이지 버튼에 해당 페이지의 순서에 해당하는 데이터 보이는 클릭 이벤트 추가
       */
        $(document).on('click','.pageBtn1', function(){

            const page = $(this).text();
            findDBFestivalList(page);

        })


        /**
       * db 다중조건 지역 시간 넣기
       */
         function findAllRegionAndTime(){

            $.ajax({

                url: '/admin/festival-regulate/findAllRegionAndTime',
                method: 'POST',
                contentType: 'application/json',
                success: function(map){

                    var list1 = map.regionList;
                    var list2 = map.timeList;

                    $('#searchRegion').html("");
                    $('#searchRegion').append(`<option value="" selected>지역</option>`);
                    $.each(list1, function(index, region){

                        var htmlContent = `<option value="${region.regionId}">${region.regionName}</option>`;
                        $('#searchRegion').append(htmlContent);

                    })

                    $('#searchTime').html("");
                    $('#searchTime').append(`<option value="" selected>시간대</option>`);

                    $.each(list2, function(index, time){

                        var htmlContent = `<option value="${time.timeId}">${time.timeDescription}</option>`;
                        $('#searchTime').append(htmlContent);

                    })

                }

            })

        }


        findAllRegionAndTime();

        /**
       * db 다중조건을 이용힌 검색 버튼 클릭
       */
         $(document).on('click','#searchBtn1', function(e){

            e.preventDefault();

            findDBFestivalByMultiple(1);
//            findDBFestivalMultipleCount();

         })

        /**
       * 다중 조건을 담은 폼 데이터 보내기
       * @param {int} page 페이지
       *
       */
         function findDBFestivalByMultiple(page1){

            console.log("findDBFestivalByMultiple(page)");
            console.log(page1);

            var regionId = document.getElementById('searchRegion').options[document.getElementById('searchRegion').selectedIndex].value;
            console.log(regionId);
            var timeId = document.getElementById('searchTime').options[document.getElementById('searchTime').selectedIndex].value;
            console.log(timeId);
            var festivalName = $('#festivalName').val();
            var festivalContent = $('#festivalContent').val();
            var manageInstitution = $('#manageInstitution').val();
            var hostInstitution = $('#hostInstitution').val();
            var tel = $('#tel').val();
            var place = $('#place').val();
            var formattedStart = $('#formattedStart').val();
            var formattedEnd = $('#formattedEnd').val();
            var avgRate = $('#avgRate').val();
            console.log(avgRate);
            var season = document.getElementById('searchSeason1').options[document.getElementById('searchSeason1').selectedIndex].value;


            console.log(season)

            var data = {

                    regionId : regionId,
                    timeId : timeId,
                    festivalName : festivalName,
                    festivalContent : festivalContent,
                    manageInstitution : manageInstitution,
                    hostInstitution : hostInstitution,
                    tel : tel,
                    place : place,
                    formattedStart : formattedStart,
                    formattedEnd : formattedEnd,
                    avgRate : avgRate,
                    season : season,
                    page : page1

                };


            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalByMultiple',
                method: 'POST',
                contentType: 'application/json',
                data : JSON.stringify(data),
                success: function(list){

                    $('#list1').html("");

                    $.each(list, function(index, festival){

                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "없음";

                        var firstHtml= ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap">

                            `;

                        }else{

                            firstHtml = `
                                <div class="card" id="${festival.festivalId}">
                                    <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap">

                            `;

                        }


                        var secondHtml = `
                            <div class="card-body">
                                  <h5 class="card-title" title="${festival.festivalName}">${festival.festivalName}</h5>
                                  <p class="card-text" title="${festival.festivalContent}">${festival.festivalContent}</p>
                                  <p class="card-text">${start} - ${end}</p>
                                  <p class="card-text t2" style="text-align:center;">★ ${festival.avgRate.toFixed(1)}</p>
                                  <div class="buttonZone">
                                    <button class="whiteHeartBtn">
                                        <img src="https://kr.object.ncloudstorage.com/team3/common/upNo.png"
                                           style="width : 25px; height: 25px;" class="heart" value="${festival.festivalId}">
                                    </button>

                                    <button class="whiteHateBtn">
                                        <img src="https://kr.object.ncloudstorage.com/team3/common/downNo.png"
                                           style="width : 25px; height: 25px;" class="hate" value="${festival.festivalId}">
                                    </button>
                                  </div>
                            </div>

                        </div>
                        `;


                        var finalHtml = firstHtml + secondHtml;

                        $('#list1').append(finalHtml);

                        findLoveList();
                        findHateList();

                    })


                }

            })

         }

         /**
        * 다중 조건 검색에 해당하는 전체 갯수를 구하고 그 수만큼 페이지 버튼 붙히기
        * @param {Array<{name: string, value: string}>} data1 직렬화 된 폼 데이터
        */
         function findDBFestivalMultipleCount(){

            console.log("findDBFestivalMultipleCount()");
            var regionId = document.getElementById('searchRegion').options[document.getElementById('searchRegion').selectedIndex].value;
            console.log(regionId);
            var timeId = document.getElementById('searchTime').options[document.getElementById('searchTime').selectedIndex].value;
            console.log(timeId);
            var festivalName = $('#festivalName').val();
            var festivalContent = $('#festivalContent').val();
            var manageInstitution = $('#manageInstitution').val();
            var hostInstitution = $('#hostInstitution').val();
            var tel = $('#tel').val();
            var place = $('#place').val();
            var formattedStart = $('#formattedStart').val();
            var formattedEnd = $('#formattedEnd').val();
            var avgRate = $('#avgRate').val();
            console.log(avgRate);
            var season = document.getElementById('searchSeason1').options[document.getElementById('searchSeason1').selectedIndex].value;


            console.log(season)

            var data = {

                    regionId : regionId,
                    timeId : timeId,
                    festivalName : festivalName,
                    festivalContent : festivalContent,
                    manageInstitution : manageInstitution,
                    hostInstitution : hostInstitution,
                    tel : tel,
                    place : place,
                    formattedStart : formattedStart,
                    formattedEnd : formattedEnd,
                    avgRate : avgRate,
                    season : season

                };


            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalMultipleCount',
                method: 'POST',
                contentType: 'application/json',
                data : JSON.stringify(data),
                success: function(count){

                    $('#pageNum1').html("");
                    // 상세검색 전으로 돌아가기 버튼
                    $('#pageNum1').append(`<button class="pageBtn4 btn btn-outline-success">전체</button>`);

                    var page = 0;

                    if(count % 5 ==0){
                        page = count/5;
                    }else{
                        page = count/5 +1;
                    }

                    for(var p=1; p<=page; p++){

                        $('#pageNum1').append(`<button class="pageBtn3 btn btn-outline-success">${p}</button>`)

                    }



                }

            })


        }


        $(document).on('click','.pageBtn3', function(){

            const page = $(this).text();
            findDBFestivalByMultiple(page);
//            findDBFestivalMultipleCount();

        })





        /**
        * 상세 조건 검색에서 전체검색으로 돌아가기 버튼
        *
        */
        $(document).on('click','.pageBtn4', function(){

            $('#list1').html("");
            window.location.href='/festival/festival-list';

        })



        /**
        * 상세 조건 검색에서 초기화 버튼으로 돌아가기 버튼
        *
        */
        $(document).on('click','#resetBtn1', function(){

            $('#list1').html("");
             window.location.href='/festival/festival-list';
        })




        /**
        * 찜하기 / 찜 취소하기 버튼
        *
        */
        $(document).on('click','.heart', function(e){

            e.stopImmediatePropagation();


            var img = $(this);
            var festivalId = img.attr('value');

            if(img.closest(".card").hasClass("hasLove")){

                $.ajax({

                    url: '/festival/deleteUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        img.closest('.card').removeClass("hasLove");
                        findLoveList();
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(img.closest(".card").hasClass("hasHate")){
                    img.closest(".card").find(".hate").click();
                }


                $.ajax({

                    url: '/festival/insertUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        img.closest(".card").addClass("hasLove");
                        findLoveList();


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }





        })


        /**
        * 찜 리스트 찾아서 up 이미지 색 바꾸기
        *
        */
        function findLoveList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findLoveList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $.each(cardList, function(index, card){

                        var cardString = card.getAttribute('id');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasLove");
                            var heartImg = $(card).find('.heart');
                            heartImg.attr('src','https://kr.object.ncloudstorage.com/team3/common/upBlue.png')

                        }else{

                            var heartImg = $(card).find('.heart');
                            heartImg.attr('src','https://kr.object.ncloudstorage.com/team3/common/upNo.png');
                        }
                    })
                }
            })

        }






       /**
        * 관심없음 하기 / 관심없음 취소하기 버튼
        *
        */
        $(document).on('click','.hate', function(e){

            e.stopImmediatePropagation();

            var img = $(this);
            var festivalId = img.attr('value');

            if(img.closest(".card").hasClass("hasHate")){

                $.ajax({

                    url: '/festival/deleteUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        img.closest('.card').removeClass("hasHate");
                        findHateList();
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(img.closest(".card").hasClass("hasLove")){
                    img.closest(".card").find(".heart").click();
                }

                $.ajax({

                    url: '/festival/insertUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        img.closest(".card").addClass("hasHate");
                        findHateList();


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }

        })


        /**
        * 관심없음 리스트 찾아서 down 이미지 색 바꾸기
        *
        */
        function findHateList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findHateList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $.each(cardList, function(index, card){

                        var cardString = card.getAttribute('id');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasHate");
                            var heartImg = $(card).find('.hate');
                            heartImg.attr('src','https://kr.object.ncloudstorage.com/team3/common/downRed.png')

                        }else{

                            var heartImg = $(card).find('.hate');
                            heartImg.attr('src','https://kr.object.ncloudstorage.com/team3/common/downNo.png');
                        }

                    })
                }
            })

        }



        //#endregion



        //#region 상단바 클릭 이벤트

        /**
        * 전체 클릭 시
        *
        */
        $(document).on('click','#allList', function(){

            $('#festivalList').removeClass('none');
            $('#festivalRecommendation').addClass('none');
            $('.extra-filter').removeClass('none');

            $('#allList').closest('.genre-item').addClass('active');
            $('#keywordList').closest('.genre-item').removeClass('active');

        })

        /**
        * 키워드 추천 클릭 시
        *
        */
        $(document).on('click','#keywordList', function(){

            $('#festivalList').addClass('none');
            $('#festivalRecommendation').removeClass('none');
            $('.extra-filter').addClass('none');

            $('#allList').closest('.genre-item').removeClass('active');
            $('#keywordList').closest('.genre-item').addClass('active');

        })


        //#endRegion




    }
)
