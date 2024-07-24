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

                                <td class="buttonHead">
                                    <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br>
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
                            <td class="imgUrl"></td>
                            <td class="buttonHead">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">키워드추가</button>
                            </td>
                            </tr>
                            `

                        }else{

                             htmlContent2 = `
                             <td class="imgUrl"><img src="${festival.imgUrl}" width="40px" alt="이미지 없음"></td>
                             <td class="buttonHead">
                                 <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                 <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">키워드추가</button>
                             </td>
                             </tr>
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

                                <td class="buttonHead">
                                    <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br>
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
                            <td class="imgUrl"></td>

                            <td class="buttonHead">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">키워드추가</button>
                            </td>
                            </tr>
                            `

                        }else{

                             htmlContent2 = `
                             <td class="imgUrl"><img src="${festival.imgUrl}" width="40px" alt="이미지 없음"></td>

                             <td class="buttonHead">
                                 <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                 <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">키워드추가</button>
                             </td>
                             </tr>
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



