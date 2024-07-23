
$(document).ready(
    function(){


        //#region DB 관련 함수

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

                    console.log(list)

                    $.each(list, function(index, festival){

                        console.log(festival)

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


                        var htmlContent11 =`

                            <tr>

                                <td>

                                    <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br><hr>

                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세 수정</button><br><hr>

                                    <button type="button" class="btn btn-primary"  id="contentKeywordInsertBtn" value="${festival.festivalId}">내용 키워드 추가</button>

                                </td>
                                <td class="index1" style="width : 20px; height: 20px;">${index1}</td>
                                <td class="festivalId">${festival.festivalId}</td>
                                <td class="regionId">${festival.regionId}</td>
                                <td class="timeId">${festival.timeId}</td>
                                <td class="festivalName">${festival.festivalName}</td>
                                <td class="festivalContent">${content}</td>
                                <td class="manageInstitution">${inst1}</td>
                                <td class="hostInstitution">${inst2}</td>
                                <td class="sponserInstitution">${inst3}</td>
                                <td class="tel">${festival.tel}</td>`;

                        var htmlContent12 =``;

                        if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                            htmlContent12 = `

                                <td class="homepageUrl"></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="formattedStart">${festival.formattedStart}</td>
                                <td class="formattedEnd">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>


                            `;

                        }else{

                           htmlContent12 = `

                                <td class="homepageUrl"><a href="${festival.homepageUrl}">클릭!</a></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="formattedStart">${festival.formattedStart}</td>
                                <td class="formattedEnd">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>

                            `;


                        }


                        var htmlContent2 = ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            htmlContent2= `
                            <td class="imgUrl"></td></tr>
                            `

                        }else{

                             htmlContent2 = `
                             <td class="imgUrl"><img src="${festival.imgUrl}" width="40px"></td></tr>
                            `
                        }


                        var finalHtml = htmlContent11 + htmlContent12 + htmlContent2;

                        $('#list1').append(finalHtml);



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
       * 선택한 DB 축제 내용에 해당하는 키워드 추가 기능
       */
        $(document).on('click','#contentKeywordInsertBtn', function(){

            var festivalId = $(this).val();

            $.ajax({

                url: '/admin/festival-regulate/insertContentKeywordByFestivalId?festivalId=' + festivalId,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $.each(list, function(index, keyword){

                        console.log(keyword);

                    })


                }


            })


        })


        /**
       * 선택한 리스트 일괄 삭제 기능
       */
        $(document).on('click','#dbDeleteBtn', function(){

            var checks = [];

            var selectedChecks = document.querySelectorAll("input[type='checkbox'].check1:checked");

            $.each(selectedChecks, function(index, item){


                checks.push($(item).val());
                console.log($(item).val());

            })

            console.log(checks)

            $.ajax({

                url: '/admin/festival-regulate/deleteDBFestivalList',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(checks),
                success: function(response){

                    alert(response);
                    window.location.href='/admin/festival-regulate';

                }


            })


        })

        /**
       * 상세 수정 모달을 클릭 시 보여주기
       */
        $(document).on('click','#dbUpdateBtn', function(){

            var festivalId = $(this).val();
            console.log("festivalId : ")
            console.log(festivalId)
            console.log("#dbUpdateBtn")



            $.ajax({

                url: '/admin/festival-regulate/findDBFestivalByFestivalId',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ festivalId : festivalId}),
                success: function(festival){

                    $('#modalBody').html("");
                        var htmlContent2 = `

                            <form action="/admin/festival-regulate/updateDBFestivalByFestival" method="post">

                                DB아이디 : <input name="festivalId" type="text" class="neverClickable" value="${festival.festivalId}"><br><br>

                                지역 번호 : <input name="regionId" type="text" class="neverClickable" value="${festival.regionId}"><br><br>

                                시간대 : <input class="neverClickable" name="timeId"  type="text" value="${festival.timeId}"><br><br>

                                축제 이름 : <input class="neverClickable" name="festivalName" type="text" value="${festival.festivalName}"><br><br>

                                상세 내용 <br><textarea name="festivalContent" cols="55" rows="10">${festival.festivalContent}</textarea><br><br>

                                주체기관 : <input class="autoWidth" name="manageInstitution" type="text" value="${festival.manageInstitution}"><br><br>

                                주관기관 : <input class="autoWidth" name="hostInstitution" type="text" value="${festival.hostInstitution}"><br><br>

                                후원기관 : <input class="autoWidth" name="sponserInstitution" type="text" value="${festival.sponserInstitution}"><br><br>

                                전화번호 : <input name="tel" type="text" value="${festival.tel}"><br><br>

                                홈페이지 : <input name="homepageUrl" type="text" value="${festival.homepageUrl}"><br><br>

                                상세주소 : <input name="detailAddress" type="text" value="${festival.detailAddress}"><br><br>

                                장소 : <input name="place" type="text" value="${festival.place}"><br><br>

                                시작일 : <input name="formattedStart" type="date" value="${festival.formattedStart}"><br><br>

                                종료일 : <input name="formattedEnd" type="date"  value="${festival.formattedEnd}"><br><br>

                                리뷰 평균 별점 : <input name="avgRate" type="text" class="neverClickable"  value="${festival.avgRate}"><br><br>

                                계절: <input class="neverClickable" name="season" type="text" value="${festival.season}"><br><br>

                                <img src="${festival.imgUrl}" width="400px"><br>
                                이미지 소스:  <input class="autoWidth" name="imgUrl" type="text" value="${festival.imgUrl}"><br><br>

                                <button type="submit" id="updateSubmitBtn">수정내용 제출</button>

                            </form>

                        `;

                        $('#modalBody').html(htmlContent2);

                }


            })


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


                        var htmlContent11 =`

                            <tr>

                                <td>

                                    <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br><hr>

                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세 수정</button><br><hr>

                                    <button type="button" class="btn btn-primary"  id="contentKeywordInsertBtn" value="${festival.festivalId}">내용 키워드 추가</button>

                                </td>
                                <td class="index1" style="width : 20px; height: 20px;">${index1}</td>
                                <td class="festivalId">${festival.festivalId}</td>
                                <td class="regionId">${festival.regionId}</td>
                                <td class="timeId">${festival.timeId}</td>
                                <td class="festivalName">${festival.festivalName}</td>
                                <td class="festivalContent">${content}</td>
                                <td class="manageInstitution">${inst1}</td>
                                <td class="hostInstitution">${inst2}</td>
                                <td class="sponserInstitution">${inst3}</td>
                                <td class="tel">${festival.tel}</td>`;

                        var htmlContent12 =``;

                        if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                            htmlContent12 = `

                                <td class="homepageUrl"></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="formattedStart">${festival.formattedStart}</td>
                                <td class="formattedEnd">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>


                            `;

                        }else{

                           htmlContent12 = `

                                <td class="homepageUrl"><a href="${festival.homepageUrl}">클릭!</a></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="formattedStart">${festival.formattedStart}</td>
                                <td class="formattedEnd">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>

                            `;


                        }


                        var htmlContent2 = ``;

                        if(! festival.imgUrl || festival.imgUrl == "null"){

                            htmlContent2= `
                            <td class="imgUrl"></td></tr>
                            `

                        }else{

                             htmlContent2 = `
                             <td class="imgUrl"><img src="${festival.imgUrl}" width="40px"></td></tr>
                            `
                        }


                        var finalHtml = htmlContent11 + htmlContent12 + htmlContent2;

                        $('#list1').append(finalHtml);



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

                    $(document).on('click','.pageBtn4', function(){

                        findDBFestivalList(1);
                        findDBFestivalCount();

                    })



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


        //#endregion





        //#region API 관련 함수

        /**
         * 전체 API리스트를 페이지 번호에 따라 가져옴
         * @param {int} page 페이지
         */
        function findAPIFestivalList(page){

            $('#list2').html("");

            $.ajax({

                url: '/admin/festival-regulate/findAPIFestivalList?page=' + page,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    $('#list2').html("");

                    $.each(list, function(index, festival){

                        var index1 = (index + 1) + (page-1)*5;
                        var indexFromZero = index1 - 1;

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




                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "링크없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "링크없음";


                        var htmlCheck = ``;

                        if(festival.exist=="Y"){

                            htmlCheck= `<tr>
                                <td>DB<br>존재</td>`


                        }else{

                             htmlCheck = `<tr>
                                <td><input class="check2" type="checkbox" name="index" value="${indexFromZero}"/></td>`
                        }

                        var htmlContent11 =`
                                <td class="index1">${index1}</td>
                                <td class="regionId">${festival.regionId}</td>
                                <td class="timeId">${festival.timeId}</td>
                                <td class="festivalName">${festival.festivalName}</td>
                                <td class="festivalContent">${content}</td>
                                <td class="manageInstitution">${inst1}</td>
                                <td class="hostInstitution">${inst2}</td>
                                <td class="sponserInstitution">${inst3}</td>
                                <td class="tel">${festival.tel}</td>`;

                        var htmlContent12 =``;


                        if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                            htmlContent12 = `

                                <td class="homepageUrl"></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="startDate">${festival.formattedStart}</td>
                                <td class="endDate">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>


                            `;

                        }else{

                            htmlContent12 = `

                                <td class="homepageUrl"><a href="${festival.homepageUrl}">클릭!</a></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="startDate">${festival.formattedStart}</td>
                                <td class="endDate">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>


                            `;

                        }

    //                        var htmlContent2 = ``;

    //                        if(! festival.imgUrl || festival.imgUrl == "null"){
    //
    //                            htmlContent2= `
    //                            <td class="imgUrl"></td></tr>
    //                            `
    //
    //
    //                        }else{
    //
    //                             htmlContent2 = `
    //                             <td class="imgUrl"><img src="${festival.imgUrl}" width="40px"></td></tr>
    //                            `
    //                        }



                        var finalHtml = htmlCheck + htmlContent11 + htmlContent12;

                        $('#list2').append(finalHtml);

                    })


                }

            })
        }


        /**
         * 전체 API리스트의 페이지 갯수를 결정 후 페이지 버튼 추가
         * @param {int} page 페이지
         */
        function findAPIFestivalCount(page){

            $('#pageNum2').html("");

           for(var p=1; p<=page; p++){

            $('#pageNum2').append(`<button class="pageBtn2">${p}</button>`)

            }

            $(document).on('click','.pageBtn2', function(){

                const page = $(this).text();
                findAPIFestivalList(page);

            })
        }

        // 처음 들어가면 1페이지 불러옴
        findAPIFestivalList(1);

        // 처음 들어가면 페이지 버튼 20개 생성
        findAPIFestivalCount(20);

        /**
         * 체크된 API 리스트의 축제들을 DB에 저장함
         */
        $(document).on('click','#apiInsertBtn', function(){

            var checks = [];

            var selectedChecks = document.querySelectorAll("input[type='checkbox'].check2:checked");

            $.each(selectedChecks, function(index, item){


                checks.push($(item).val());
                console.log($(item).val());

            })

            console.log(checks)

            $.ajax({

                url: '/admin/festival-regulate/insertAPIFestivalList',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(checks),
                success: function(response){

                    alert(response);
                    window.location.href='/admin/festival-regulate';

                }


            })


        })


        /**
         * api 다중조건 검색 모달 클릭
         */
         $(document).on('click','#search2', function(){

            $.ajax({

                url: '/admin/festival-regulate/findAllRegionAndTime',
                method: 'POST',
                contentType: 'application/json',
                success: function(map){

                    var list1 = map.regionList;

                    $('#searchRegion2').html("");

                    $.each(list1, function(index, region){

                        var htmlContent = `<option value="${region.regionId}">${region.regionName}</option>`;
                        $('#searchRegion2').append(htmlContent);

                    })

                }

            })

        })


         /**
          * api 다중조건 검색 버튼 클릭
          */
         $(document).on('click','#searchBtn2', function(){

            var data = $("#searchForm2").serializeArray();

            console.log("폼 : ");

            console.log(data);


            findAPIFestivalByMultiple(data,1);
            findAPIFestivalMultipleCount(data);

            // 다중 조건 검색 모달 닫기
            $('#close2').click();

         })



          /**
           * API 수정 조건 폼 보내기
           * @param {Array<{name: string, value: string}>} data2 직렬화 된 폼 데이터
           * @param {int} page 페이지
           */
         function findAPIFestivalByMultiple(data2, page){

            console.log(data2);

            $.ajax({

                url: '/admin/festival-regulate/findAPIFestivalByMultiple?page=' + page,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data2),
                success: function(list){

                    $('#list2').html("");

                    $.each(list, function(index, festival){

                        var index1 = (index + 1) + (page-1)*5;
                        var indexFromZero = index1 - 1;

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


                        var start = festival.startDate.length >0?
                        festival.startDate.substring(0,10):
                        "링크없음";

                        var end = festival.endDate.length >0?
                        festival.endDate.substring(0,10):
                        "링크없음";

                        var htmlCheck = ``;

                        if(festival.exist=="Y"){

                            htmlCheck= `<tr>
                                <td>DB<br>존재</td>`


                        }else{

                             htmlCheck = `<tr>
                                <td><input class="check2" type="checkbox" name="index" value="${indexFromZero}"/></td>`
                        }


                        var htmlContent =`

                                <td class="index1">${index1}</td>
                                <td class="regionId">${festival.regionId}</td>
                                <td class="timeId">${festival.timeId}</td>
                                <td class="festivalName">${festival.festivalName}</td>
                                <td class="festivalContent">${content}</td>
                                <td class="manageInstitution">${inst1}</td>
                                <td class="hostInstitution">${inst2}</td>
                                <td class="sponserInstitution">${inst3}</td>
                                <td class="tel">${festival.tel}</td>
                                <td class="homepageUrl"><a href="${festival.homepageUrl}">클릭!</a></td>
                                <td class="detailAddress">${festival.detailAddress}</td>

                                <td class="place">${festival.place}</td>
                                <td class="startDate">${festival.formattedStart}</td>
                                <td class="endDate">${festival.formattedEnd}</td>
                                <td class="avgRate">${festival.avgRate}</td>
                                <td class="season">${festival.season}</td>

                            </tr>

                        `;

                        var finalHtml = htmlCheck + htmlContent;

                        $('#list2').append(finalHtml);



                    })


                }

            })

         }



         /**
        * API 상세검색 갯수 알아와서 페이지 버튼 갯수만큼 붙히기
        * @param {Array<{name: string, value: string}>} data2 직렬화 된 폼 데이터
        */
         function findAPIFestivalMultipleCount(data2){

            $.ajax({

                url: '/admin/festival-regulate/findAPIFestivalMultipleCount',
                method: 'POST',
                data: JSON.stringify(data2),
                contentType: 'application/json',
                success: function(count){

                    $('#pageNum2').html("");
                    // 상세검색 전으로 돌아가기 버튼
                    $('#pageNum2').append(`<button class="pageBtn6">전체</button>`);

                    console.log("카운트는...")
                    console.log(count)

                    var page = 0;

                    if(count % 5 ==0){
                        page = count/5;
                    }else{
                        page = count/5 +1;
                    }

                    for(var p=1; p<=page; p++){

                        $('#pageNum2').append(`<button class="pageBtn5">${p}</button>`)

                    }



                    $(document).on('click','.pageBtn5', function(){

                        const page = $(this).text();
                        findAPIFestivalByMultiple(data2, page);

                    })


                }

            })


        }

        /**
        * 전체 버튼으로 API 상세검색에서 API 전체리스트로 돌아가는 클릭 이벤트
        */
        $(document).on('click','.pageBtn6', function(){

            $('#list2').html("");
            console.log("pageBtn6")
            findAPIFestivalList(1);
            findAPIFestivalCount(20);

        })


        //#endregion




    }
)


