$(document).ready(
    function(){


        //#region festivalList 관련 함수

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

                    console.log(list);
                    $.each(list, function(index, festival){

                        var index1 = (index + 1) + (page-1)*5;


                        var content = festival.festivalContent.length > 30?
                        festival.festivalContent.substring(0,30) + "..."
                        : festival.festivalContent;

                        var inst1 = festival.manageInstitution.length > 15?
                        festival.manageInstitution.substring(0,15) + "..."
                        : festival.manageInstitution;

                        var inst2 = festival.hostInstitution.length > 15?
                        festival.hostInstitution.substring(0,15) + "..."
                        : festival.hostInstitution;

                        var inst3 = festival.sponserInstitution.length > 15?
                        festival.sponserInstitution.substring(0,15) + "..."
                        : festival.sponserInstitution;


                        console.log("축제 날짜")
                        console.log(festival.startDate);
                        console.log(festival.endDate);

                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "없음";




                        var firstHtml= ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            firstHtml = `
                                <div class="card" style="width: 18rem;" id="${festival.festivalId}">
                                    <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap" style="min-width: 380px; min-height: 400px;">

                            `;

                        }else{

                            firstHtml = `
                                <div class="card" style="width: 18rem;" id="${festival.festivalId}">
                                    <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap" style="min-width: 380px; min-height: 400px;">

                            `;

                        }



                        var secondHtml = `
                            <div class="card-body">
                                  <h5 class="card-title">${festival.festivalName}</h5>
                                  <p class="card-text">${content}</p>
                                  <a href="/festival/festival-detail?festivalId=${festival.festivalId}" class="btn btn-primary">자세히</a>
                                  <button class="btn btn-primary heart" type="button" style="margin:0px" value="${festival.festivalId}">
                                      <img src="/img/festival/heart.png"
                                           style="width : 20px; height: 20px;">
                                  </button>
                                  <button class="btn btn-primary hate" type="button" style="margin:0px" value="${festival.festivalId}">
                                        <img src="/img/festival/trash.png"
                                             style="width : 20px; height: 20px;">
                                  </button>
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

        // 1페이지 호출
        findDBFestivalList(1);

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
                    console.log("카운트는...")
                    console.log(count)

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

        // 버튼 붙히기
        findDBFestivalCount();

        /**
       * DB 페이지 버튼에 해당 페이지의 순서에 해당하는 데이터 보이는 클릭 이벤트 추가
       */
        $(document).on('click','.pageBtn1', function(){

            const page = $(this).text();
            findDBFestivalList(page);

        })


        /**
       * db 다중조건 검색 모달 클릭
       */
         $(document).on('click','#search1', function(){

            $.ajax({

                url: '/admin/festival-regulate/findAllRegionAndTime',
                method: 'POST',
                contentType: 'application/json',
                success: function(map){

                    var list1 = map.regionList;
                    var list2 = map.timeList;

                    $('#searchRegion1').html("");
                    $('#searchRegion1').append(`<option value="">선택안함</option>`);

                    $.each(list1, function(index, region){

                        var htmlContent = `<option value="${region.regionId}">${region.regionName}</option>`;
                        $('#searchRegion1').append(htmlContent);

                    })

                    $('#searchTime1').html("");
                    $('#searchTime1').append(`<option value="">선택 안함</option>`);

                    $.each(list2, function(index, time){

                        var htmlContent = `<option value="${time.timeId}">${time.timeDescription}</option>`;
                        $('#searchTime1').append(htmlContent);

                    })

                }

            })

        })

        /**
       * db 다중조건을 이용힌 검색 버튼 클릭
       */
         $(document).on('click','#searchBtn1', function(){

            var data = $("#searchForm1").serializeArray();

            console.log("폼 : ");

            console.log(data);


            findDBFestivalByMultiple(data,1);
            findDBFestivalMultipleCount(data);

            // 다중 조건 검색 모달 닫기
            $('#close1').click();

         })

        /**
       * 다중 조건을 담은 폼 데이터 보내기
       * @param {int} page 페이지
       * @param {Array<{name: string, value: string}>} data1 직렬화 된 폼 데이터
       */
         function findDBFestivalByMultiple(data1, page){


            console.log(data1);

            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalByMultiple?page=' + page,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data1),
                success: function(list){

                    $('#close1').click();

                    $('#list1').html("");


                    $.each(list, function(index, festival){

                        console.log(festival);
                        var index1 = (index + 1) + (page-1)*5;

                        var content = festival.festivalContent.length > 30?
                        festival.festivalContent.substring(0,30) + "..."
                        : festival.festivalContent;

                        var inst1 = festival.manageInstitution.length > 15?
                        festival.manageInstitution.substring(0,15) + "..."
                        : festival.manageInstitution;

                        var inst2 = festival.hostInstitution.length > 15?
                        festival.hostInstitution.substring(0,15) + "..."
                        : festival.hostInstitution;

                        var inst3 = festival.sponserInstitution.length > 15?
                        festival.sponserInstitution.substring(0,15) + "..."
                        : festival.sponserInstitution;


                        console.log("축제 날짜")
                        console.log(festival.startDate);
                        console.log(festival.endDate);

                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "없음";


                        var firstHtml= ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            firstHtml = `
                                <div class="card" style="width: 18rem;" id="${festival.festivalId}">
                                    <img class="card-img-top" src="/img/festival/noPhoto.png" alt="Card image cap" style="min-width: 380px; min-height: 400px;">

                            `;

                        }else{

                            firstHtml = `
                                <div class="card" style="width: 18rem;" id="${festival.festivalId}">
                                    <img class="card-img-top" src="${festival.imgUrl}" alt="Card image cap" style="min-width: 380px; min-height: 400px;">

                            `;

                        }



                        var secondHtml = `
                            <div class="card-body">
                                  <h5 class="card-title">${festival.festivalName}</h5>
                                  <p class="card-text">${content}</p>
                                  <a href="/festival/festival-detail?festivalId=${festival.festivalId}" class="btn btn-primary">자세히</a>
                                  <button class="btn btn-primary heart" type="button" style="margin:0px" value="${festival.festivalId}">
                                      <img src="/img/festival/heart.png"
                                           style="width : 20px; height: 20px;">
                                  </button>
                                  <button class="btn btn-primary hate" type="button" style="margin:0px" value="${festival.festivalId}">
                                        <img src="/img/festival/trash.png"
                                             style="width : 20px; height: 20px;">
                                  </button>
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
         function findDBFestivalMultipleCount(data1){

            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalMultipleCount',
                method: 'POST',
                data: JSON.stringify(data1),
                contentType: 'application/json',
                success: function(count){

                    $('#pageNum1').html("");
                    // 상세검색 전으로 돌아가기 버튼
                    $('#pageNum1').append(`<button class="pageBtn4">전체</button>`);


                    console.log("카운트는...")
                    console.log(count)

                    var page = 0;

                    if(count % 5 ==0){
                        page = count/5;
                    }else{
                        page = count/5 +1;
                    }

                    for(var p=1; p<=page; p++){

                        $('#pageNum1').append(`<button class="pageBtn3">${p}</button>`)

                    }

                    $(document).on('click','.pageBtn3', function(){

                        const page = $(this).text();
                        findDBFestivalByMultiple(data1, page);

                    })

                }

            })


        }


        /**
        * 상세 조건 검색에서 전체검색으로 돌아가기 버튼
        *
        */
        $(document).on('click','.pageBtn4', function(){

            $('#list1').html("");
            findDBFestivalList(1);
            findDBFestivalCount();

        })




        /**
        * 찜하기 / 찜 취소하기 버튼
        *
        */
        $(document).on('click','.heart', function(){

            var button = $(this);
            var festivalId = $(this).val();

            if(button.closest(".card").hasClass("hasLove")){

                $.ajax({

                    url: '/festival/deleteUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        button.closest('.card').removeClass("hasLove");
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(button.closest(".card").hasClass("hasHate")){
                    button.closest(".card").find(".hate").click();
                }


                $.ajax({

                    url: '/festival/insertUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        button.closest(".card").addClass("hasLove");
                        console.log("addClass('hasLove');")


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }





        })


        /**
        * 찜 리스트 찾아서 색 바꾸기
        *
        */
        function findLoveList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findLoveList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    console.log(list);
                    $.each(cardList, function(index, card){

                        console.log("card.getAttribute('id')")
                        console.log(card.getAttribute('id'));

                        var cardString = card.getAttribute('id');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasLove");

                        }
                    })
                }
            })

        }






       /**
        * 관심없음 하기 / 관심없음 취소하기 버튼
        *
        */
        $(document).on('click','.hate', function(){

            var button = $(this);
            var festivalId = $(this).val();

            if(button.closest(".card").hasClass("hasHate")){

                $.ajax({

                    url: '/festival/deleteUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        button.closest('.card').removeClass("hasHate");
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(button.closest(".card").hasClass("hasLove")){
                    button.closest(".card").find(".heart").click();
                }

                $.ajax({

                    url: '/festival/insertUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        button.closest(".card").addClass("hasHate");
                        console.log("addClass('hasHate');")


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }





        })


        /**
        * 관심없음 리스트 찾아서 색 바꾸기
        *
        */
        function findHateList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findHateList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    console.log(list);
                    $.each(cardList, function(index, card){

                        console.log("card.getAttribute('id')")
                        console.log(card.getAttribute('id'));

                        var cardString = card.getAttribute('id');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasHate");

                        }
                    })
                }
            })

        }







        //#endregion


    }
)
