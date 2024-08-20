
$(document).ready(
    function(){


        //#region DB 관련 함수

        /**
       * DB 전체 리스트 페이지별 조회 기능
       * @param {int} page 페이지
       */
        function findDBFestivalList(page){

            return new Promise((resolve, reject)=>{

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


                            var htmlContent11 =`

                                <tr>

                                    <td class="checkHead">
                                        <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br>
                                    </td>
                                    <td class="index1" style="width : 20px; height: 20px;">${index1}</td>
                                    <td class="festivalId" title="${festival.festivalId}">${festival.festivalId}</td>
                                    <td class="regionId" title="${festival.regionId}">${festival.regionId}</td>
                                    <td class="timeId" title="${festival.timeId}">${festival.timeId}</td>
                                    <td class="festivalName" title="${festival.festivalName}">${festival.festivalName}</td>
                                    <td class="festivalContent" title="${festival.festivalContent}">${festival.festivalContent}</td>
                                    <td class="manageInstitution" title="${festival.manageInstitution}">${festival.manageInstitution}</td>
                                    <td class="tel" title="${festival.tel}">${festival.tel}</td>`;

                            var htmlContent12 =``;

                            if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                                htmlContent12 = `

                                    <td class="homepageUrl"></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>

                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="formattedStart" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="formattedEnd" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="avgRate" title="${festival.avgRate}">${festival.avgRate}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


                                `;

                            }else{

                               htmlContent12 = `

                                    <td class="homepageUrl" title="${festival.homepageUrl}"><a href="${festival.homepageUrl}">클릭!</a></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>

                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="formattedStart" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="formattedEnd" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="avgRate" title="${festival.avgRate}">${festival.avgRate}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>

                                `;


                            }


                            var htmlContent2 = ``;

                            if(! festival.imgUrl || festival.imgUrl == "null"){

                                htmlContent2= `
                                <td class="imgUrl"></td>
                                <td class="buttonHead">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                    <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">내용</button>
                                    <button type="button" class="btn btn-primary naverArticleKeywordInsertBtn" value="${festival.festivalId}">기사</button><br><br>
                                    <button type="button" class="btn btn-primary naverBlogKeywordInsertBtn" value="${festival.festivalId}">블로그</button>
                                    <button type="button" class="btn btn-primary reviewKeywordInsertBtn" value="${festival.festivalId}">리뷰</button><br>
                                </td>
                                </tr>
                                `

                            }else{

                                 htmlContent2 = `
                                 <td class="imgUrl"><img src="${festival.imgUrl}" alt="이미지 없음" style="width: 100%;
                                 height: 150px"></td>
                                 <td class="buttonHead">
                                     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                     <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">내용</button>
                                     <button type="button" class="btn btn-primary naverArticleKeywordInsertBtn" value="${festival.festivalId}">기사</button><br><br>
                                     <button type="button" class="btn btn-primary naverBlogKeywordInsertBtn" value="${festival.festivalId}">블로그</button>
                                     <button type="button" class="btn btn-primary reviewKeywordInsertBtn" value="${festival.festivalId}">리뷰</button><br>
                                 </td>
                                 </tr>
                                `
                            }


                            var finalHtml = htmlContent11 + htmlContent12 + htmlContent2;

                            $('#list1').append(finalHtml);



                        })

                        resolve();


                    }

                })

            })
        }

        // 1페이지 호출
        findDBFestivalList(1);


        var DBLastPage = 0;

        /**
       * DB 전체 갯수를 알아와 페이지 버튼 추가 기능
       * @returns {Promise} 순서대로 이행하기 위해 프로미스로 반환
       */
        function findDBFestivalCount(){

            return new Promise((resolve, reject)=>{

                $.ajax({

                    url: '/admin/festival-regulate/findDBFestivalCount',
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(count){

                        $('#pageNum1').html("");
                        $('#pageNum1').append(`<button class="pageBtn10" value="1">처음</button>`);

                        var page = 0;

                        if(count % 5 ==0){
                            page = parseInt(count/5);
                        }else{
                            page = parseInt(count/5) +1;
                        }

                        DBLastPage = page;

                        for(var p=1; p<=page; p++){

                            $('#pageNum1').append(`<button class="pageBtn1 none">${p}</button>`)

                        }

                        $('#pageNum1').append(`<button class="pageBtn11">다음</button>`);
                        $('#pageNum1').append(`<button class="pageBtn12">마지막</button>`);

                        resolve();
                    },
                    error: function(error){
                        reject(error);

                    }

                })


            })

        }


        /**
       * 현재 보여야하는 버튼만 보이게 함
       * @param {number} num 10개씩 보이게 하기 위한 숫자. 1이면 1~10
       * @param {string} className 보이게 할 버튼 클래스
       */
        function showCurrentPage(num, className){

            let btns = document.querySelectorAll(className);
            var start = (num-1)*10 + 1;
            var end = (num)*10;


            btns.forEach(function(btn){

                var btnNum = parseInt(btn.textContent);
                console.log("버튼")
                console.log(btnNum);
                if((btnNum >= start) && (btnNum <= end)){

                    btn.classList.remove('none');


                }else{
                    btn.classList.add('none');
                }
            })

            console.log("showCurrentPage")
            console.log(num);
            console.log(start);
            console.log(end);

        }




        /**
       * 현재 보여야하는 버튼만 보이게 함
       * @param {Function} countFun 전체 갯수를 세 페이지 갯수를 정하는 함수
       * @param {number} num 10개씩 보이게 하기 위한 숫자. 1이면 1~10
       * @param {string} className 보이게 할 버튼 클래스
       */
        async function addShowingBtns(countFun , num, className){

            try{
                await countFun();
                let btns = document.querySelectorAll(className);
                showCurrentPage(num, className);
            } catch(error){
                console.error(error);
            }
        }


        addShowingBtns(findDBFestivalCount, 1, '.pageBtn1');


        /**
       * DB 페이지 버튼에 해당 페이지의 순서에 해당하는 데이터 보이는 클릭 이벤트 추가
       */
        $(document).on('click','.pageBtn1', function(){

            let btns = document.querySelectorAll('.pageBtn1');

            btns.forEach(function(btn){

                btn.classList.remove('active');

            })

            $(this).addClass('active');
            const page = $(this).text();
            $('#list1').html("");
            findDBFestivalList(page);

        })



        var currentDBNum = 1;



        /**
       * 처음으로 돌아가기
       */
        $(document).on('click','.pageBtn10', function(){

            $('#list1').html("");
            currentDBNum = 1;

            findDBFestivalList(1)
            .then(()=>{
                // 1~10까지 보여줌
                return addShowingBtns(findDBFestivalCount, currentDBNum, '.pageBtn1');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn1');
                btns.forEach(function(btn){
                    if(btn.textContent == "1"){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })



        /**
       * 마지막으로 가기
       */
        $(document).on('click','.pageBtn12', function(){

            $('#list1').html("");

            var num = 0;

            if(DBLastPage%10==0){
                num = parseInt(DBLastPage / 10);
            }else{
                num = parseInt((DBLastPage / 10) + 1);
            }

            currentDBNum = num;

            findDBFestivalList(DBLastPage)
            .then(()=>{
                return addShowingBtns(findDBFestivalCount, currentDBNum, '.pageBtn1');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn1');
                btns.forEach(function(btn){
                    if(btn.textContent == DBLastPage){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })


        /**
         * db리스트의 다음 페이지 버튼 클릭 이벤트
         *
         */

        $(document).on('click','.pageBtn11', function(){

            currentDBNum = currentDBNum + 1;
            var thisFirstPage = (currentDBNum-1) * 10 + 1;

            findDBFestivalList(thisFirstPage)
                .then(()=>{
                return addShowingBtns(findDBFestivalCount, currentDBNum, '.pageBtn1');
                })
                .then(()=>{

                    let btns = document.querySelectorAll('.pageBtn1');
                    btns.forEach(function(btn){
                        if(btn.textContent == thisFirstPage){
                            btn.classList.add('active');
                        }else{
                            btn.classList.remove('active');
                        }
                    })
                })
                .catch((error)=>{
                    alert("api 오류")
                })

        })









       /**
       * 선택한 DB 축제 내용에 해당하는 키워드 추가 기능
       */
        $(document).on('click','.contentKeywordInsertBtn', function(){

            var festivalId = $(this).val();

            $.ajax({

                url: '/admin/festival-regulate/insertContentKeywordByFestivalId?festivalId=' + festivalId,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    alert("내용 키워드 : " + list + ". 키워드 삽입 성공!")


                }


            })


        })



       /**
       * 선택한 DB 축제 관련 네이버 기사에 해당하는 키워드 추가 기능
       */
        $(document).on('click','.naverArticleKeywordInsertBtn', function(){

            var festivalId = $(this).val();

            $.ajax({

                url: '/admin/festival-regulate/insertNaverArticleKeywordByFestivalId?festivalId=' + festivalId,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    alert("네이버 기사 키워드 : " + list + ". 키워드 삽입 성공!")


                }


            })

        })

       /**
       * 선택한 DB 축제 관련 네이버 블로그에 해당하는 키워드 추가 기능
       */
        $(document).on('click','.naverBlogKeywordInsertBtn', function(){

            var festivalId = $(this).val();

            $.ajax({

                url: '/admin/festival-regulate/insertNaverBlogKeywordByFestivalId?festivalId=' + festivalId,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    alert("네이버 블로그 키워드 : " + list + ". 키워드 삽입 성공!")

                }


            })

        })




       /**
       * 선택한 DB 축제 관련 컬쳐링크 사이트 리뷰에 해당하는 키워드 추가 기능
       */
        $(document).on('click','.reviewKeywordInsertBtn', function(){

            var festivalId = $(this).val();
            alert("reviewKeywordInsertBtn")

            $.ajax({

                url: '/admin/festival-regulate/insertReviewKeywordByFestivalId?festivalId=' + festivalId,
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    alert("리뷰 키워드 : " + list + ". 키워드 삽입 성공!")


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

            })


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

                                <img src="${festival.imgUrl}" width="400px" alt="이미지 없음"><br>
                                이미지 소스:  <input class="autoWidth" name="imgUrl" type="text" value="${festival.imgUrl}"><br><br>

                                <button type="submit" id="updateSubmitBtn" class="btn btn-outline-primary ml-3">수정내용 제출</button>

                            </form>

                        `;

                        $('#modalBody').html(htmlContent2);

                }


            })


        })

       /**
      * 검색 인풋값 가져오기
      *
      */
       function findAllRegionAndTime(){

            $.ajax({

                url: '/admin/festival-regulate/findAllRegionAndTime',
                method: 'POST',
                contentType: 'application/json',
                success: function(map){

                    var list1 = map.regionList;
                    var list2 = map.timeList;

                    $('#searchRegion1').html("");
                    $('#searchRegion1').append(`<option value="">지역</option>`);

                    $.each(list1, function(index, region){

                        var htmlContent = `<option value="${region.regionId}">${region.regionName}</option>`;
                        $('#searchRegion1').append(htmlContent);

                    })

                    $('#searchTime1').html("");
                    $('#searchTime1').append(`<option value="">시간대</option>`);

                    $.each(list2, function(index, time){

                        var htmlContent = `<option value="${time.timeId}">${time.timeDescription}</option>`;
                        $('#searchTime1').append(htmlContent);

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
            findDBFestivalMultipleCount();

            // 다중 조건 검색 모달 닫기
            $('#close1').click();

         })

       /**
      * 다중 조건을 담은 폼 데이터 보내기
      * @param {int} page 페이지
      *
      */
        function findDBFestivalByMultiple(page){

            return new Promise((resolve, reject)=>{

                var regionId = document.getElementById('searchRegion1').options[document.getElementById('searchRegion1').selectedIndex].value;
               var timeId = document.getElementById('searchTime1').options[document.getElementById('searchTime1').selectedIndex].value;
               var festivalName = $('#festivalName1').val();
               var festivalContent = $('#festivalContent1').val();
               var manageInstitution = $('#manageInstitution1').val();
               var hostInstitution = $('#hostInstitution1').val();
               var tel = $('#tel1').val();
               var place = $('#place1').val();
               var formattedStart = $('#formattedStart1').val();
               var formattedEnd = $('#formattedEnd1').val();
               var avgRate = $('#avgRate1').val();
               var season = document.getElementById('searchSeason1').options[document.getElementById('searchSeason1').selectedIndex].value;

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
                       page : page

                   };

               var startIndex = (page-1)*5+1;
               var endIndex = (page)*5;


               $.ajax({

                   url: '/admin/festival-regulate/findDBFestivalByMultiple',
                   method: 'POST',
                   contentType: 'application/json',
                   data : JSON.stringify(data),
                   success: function(list){

                        $('#list1').html("");

                        var slicedList = list.slice(startIndex, endIndex+1);

                        $.each(slicedList, function(index, festival){

                            var index1 = (index + 1) + (page-1)*5;

                            var start = festival.startDate.length >0?
                            festival.startDate.substring(0,10):
                            "없음";

                            var end = festival.endDate.length >0?
                            festival.endDate.substring(0,10):
                            "없음";


                            var htmlContent11 =`

                                <tr>

                                    <td class="checkHead">
                                        <input class="check1" type="checkbox" name="index" value="${festival.festivalId}"/><br>
                                    </td>
                                    <td class="index1" style="width : 20px; height: 20px;">${index1}</td>
                                    <td class="festivalId" title="${festival.festivalId}">${festival.festivalId}</td>
                                    <td class="regionId" title="${festival.regionId}">${festival.regionId}</td>
                                    <td class="timeId" title="${festival.timeId}">${festival.timeId}</td>
                                    <td class="festivalName" title="${festival.festivalName}">${festival.festivalName}</td>
                                    <td class="festivalContent" title="${festival.festivalContent}">${festival.festivalContent}</td>
                                    <td class="manageInstitution" title="${festival.manageInstitution}">${festival.manageInstitution}</td>
                                    <td class="tel" title="${festival.tel}">${festival.tel}</td>`;

                            var htmlContent12 =``;

                            if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                                htmlContent12 = `

                                    <td class="homepageUrl"></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>

                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="formattedStart" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="formattedEnd" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="avgRate" title="${festival.avgRate}">${festival.avgRate}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


                                `;

                            }else{

                               htmlContent12 = `

                                    <td class="homepageUrl" title="${festival.homepageUrl}"><a href="${festival.homepageUrl}">클릭!</a></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>

                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="formattedStart" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="formattedEnd" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="avgRate" title="${festival.avgRate}">${festival.avgRate}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>

                                `;


                            }


                            var htmlContent2 = ``;

                            if(! festival.imgUrl || festival.imgUrl == "null"){

                                htmlContent2= `
                                <td class="imgUrl"></td>
                                <td class="buttonHead">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                    <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">내용</button>
                                    <button type="button" class="btn btn-primary naverArticleKeywordInsertBtn" value="${festival.festivalId}">기사</button><br><br>
                                    <button type="button" class="btn btn-primary naverBlogKeywordInsertBtn" value="${festival.festivalId}">블로그</button>
                                    <button type="button" class="btn btn-primary reviewKeywordInsertBtn" value="${festival.festivalId}">리뷰</button><br>
                                </td>
                                </tr>
                                `

                            }else{

                                 htmlContent2 = `
                                 <td class="imgUrl" style="min-width: 150px;"><img src="${festival.imgUrl}" alt="이미지 없음" style="width: 100%;height:150px"></td>
                                 <td class="buttonHead">
                                     <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalLong" id="dbUpdateBtn" value="${festival.festivalId}">상세수정</button><br><hr>
                                     <button type="button" class="btn btn-primary contentKeywordInsertBtn" value="${festival.festivalId}">내용</button>
                                     <button type="button" class="btn btn-primary naverArticleKeywordInsertBtn" value="${festival.festivalId}">기사</button><br><br>
                                     <button type="button" class="btn btn-primary naverBlogKeywordInsertBtn" value="${festival.festivalId}">블로그</button>
                                     <button type="button" class="btn btn-primary reviewKeywordInsertBtn" value="${festival.festivalId}">리뷰</button><br>
                                 </td>
                                 </tr>
                                `
                            }


                            var finalHtml = htmlContent11 + htmlContent12 + htmlContent2;

                            $('#list1').append(finalHtml);



                        })

                        resolve();
                    }

                })

            })
         }


         var lastDBMultiPage = 0;

         /**
        * 다중 조건 검색에 해당하는 전체 갯수를 구하고 그 수만큼 페이지 버튼 붙히기
        * @param {Array<{name: string, value: string}>} data1 직렬화 된 폼 데이터
        */
         function findDBFestivalMultipleCount(){

            return new Promise((resolve, reject)=>{

                var regionId = document.getElementById('searchRegion1').options[document.getElementById('searchRegion1').selectedIndex].value;
               var timeId = document.getElementById('searchTime1').options[document.getElementById('searchTime1').selectedIndex].value;
               var festivalName = $('#festivalName1').val();
               var festivalContent = $('#festivalContent1').val();
               var manageInstitution = $('#manageInstitution1').val();
               var hostInstitution = $('#hostInstitution1').val();
               var tel = $('#tel1').val();
               var place = $('#place1').val();
               var formattedStart = $('#formattedStart1').val();
               var formattedEnd = $('#formattedEnd1').val();
               var avgRate = $('#avgRate1').val();
               var season = document.getElementById('searchSeason1').options[document.getElementById('searchSeason1').selectedIndex].value;


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
                    data: JSON.stringify(data),
                    contentType: 'application/json',
                    success: function(count){

                        $('#pageNum1').html("");
                        // 상세검색 전으로 돌아가기 버튼
                        $('#pageNum1').append(`<button class="pageBtn4">전체</button>`);
                        $('#pageNum1').append(`<button class="pageBtn13" value="1">처음</button>`);

                        var page = 0;

                        if(count % 5 ==0){
                            page = parseInt(count/5);
                        }else{
                            page = parseInt(count/5 +1);
                        }

                        lastDBMultiPage = parseInt(page);

                        for(var p=1; p<=page; p++){

                            $('#pageNum1').append(`<button class="pageBtn3">${p}</button>`)

                        }

                        $('#pageNum1').append(`<button class="pageBtn14">다음</button>`);
                        $('#pageNum1').append(`<button class="pageBtn15">마지막</button>`);

                        resolve();

                    }

                })

            })

        }


        /**
       * DB 상세검색 페이지 버튼에 해당 페이지의 순서에 해당하는 데이터 보이는 클릭 이벤트 추가
       */
        $(document).on('click','.pageBtn3', function(){

            let btns = document.querySelectorAll('.pageBtn3');

            btns.forEach(function(btn){

                btn.classList.remove('active');

            })

            $(this).addClass('active');
            const page = $(this).text();
            $('#list1').html("");
            findDBFestivalByMultiple(page);

        })




        var currentDBMulNum = 1;


        /**
       * 상세검색 처음으로 돌아가기
       */
        $(document).on('click','.pageBtn13', function(){

            $('#list1').html("");
            currentDBMulNum = 1;

            findDBFestivalByMultiple(1)
            .then(()=>{
                // 1~10까지 보여줌
                return addShowingBtns(findDBFestivalMultipleCount, currentDBMulNum, '.pageBtn3');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn3');
                btns.forEach(function(btn){
                    if(btn.textContent == "1"){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })





        /**
       * 상세검색 마지막으로 가기
       */
        $(document).on('click','.pageBtn15', function(){

            $('#list1').html("");

            var num = 0;

            console.log("마지막 페이지")
            console.log(lastDBMultiPage)

            if(lastDBMultiPage%10==0){
                num = (lastDBMultiPage / 10);
            }else{
                num = parseInt((lastDBMultiPage / 10) + 1);
            }

            currentDBMulNum = num;

            findDBFestivalByMultiple(lastDBMultiPage)
            .then(()=>{
                return addShowingBtns(findDBFestivalMultipleCount, currentDBMulNum, '.pageBtn3');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn3');
                btns.forEach(function(btn){
                    if(btn.textContent == lastDBMultiPage){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })





        /**
         * db 멀티 리스트의 다음 페이지 버튼 클릭 이벤트
         *
         */

        $(document).on('click','.pageBtn14', function(){

            currentDBMulNum = currentDBMulNum + 1;
            var thisFirstPage = (currentDBMulNum-1) * 10 + 1;

            findDBFestivalByMultiple(thisFirstPage)
                .then(()=>{
                return addShowingBtns(findDBFestivalMultipleCount, currentDBMulNum, '.pageBtn3');
                })
                .then(()=>{

                    let btns = document.querySelectorAll('.pageBtn3');
                    btns.forEach(function(btn){
                        if(btn.textContent == thisFirstPage){
                            btn.classList.add('active');
                        }else{
                            btn.classList.remove('active');
                        }
                    })
                })
                .catch((error)=>{
                    alert("api 오류")
                })

        })












        /**
        * DB 상세 조건 검색에서 초기화 버튼으로 돌아가기 버튼
        *
        */
        $(document).on('click','#resetBtn1', function(){

            document.getElementById('searchForm1').reset();


        })

        $(document).on('click','.pageBtn4', function(){

            findDBFestivalList(1);
            addShowingBtns(findDBFestivalCount, 1, '.pageBtn1');

        })



        //#endregion





        //#region API 관련 함수

        /**
         * 전체 API리스트를 페이지 번호에 따라 가져옴
         * @param {int} page 페이지
         */

        function findAPIFestivalList(page){

            return new Promise((resolve, reject)=>{

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
                                    <td class="timeId" title="${festival.timeId}">${festival.timeId}</td>
                                    <td class="festivalName" title="${festival.festivalName}">${festival.festivalName}</td>
                                    <td class="festivalContent" title="${festival.festivalContent}">${festival.festivalContent}</td>
                                    <td class="manageInstitution" title="${festival.manageInstitution}">${festival.manageInstitution}</td>
                                    <td class="tel" title="${festival.tel}">${festival.tel}</td>`;

                            var htmlContent12 =``;


                            if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                                htmlContent12 = `

                                    <td class="homepageUrl"></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>
                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="startDate" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="endDate" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


                                `;

                            }else{

                                htmlContent12 = `

                                    <td class="homepageUrl" title="${festival.homepageUrl}"><a href="${festival.homepageUrl}">클릭!</a></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>
                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="startDate" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="endDate" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


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
                        resolve();
                    }
                })
            })
        }



        var findAPIFestivalNumPage = 0;
        var current = 1;
        /**
         * 전체 API리스트의 페이지 갯수를 결정 후 페이지 버튼 추가
         * @param {int} page 페이지
         */
        function findAPIFestivalCount(){

            return new Promise((resolve, reject)=>{

                $.ajax({

                    url: '/admin/festival-regulate/findAPIFestivalCount',
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(count){

                        findAPIFestivalNum = count;
                        console.log("api 전체 갯수")
                        console.log(count);

                        $('#pageNum2').html("");
                        console.log("page");
                        $('#pageNum2').append(`<button class="pageBtn7" value="1">처음</button>`)

                        var page = 0;

                        if(count%5==0){
                            page = parseInt(count / 5);
                        }else{
                            page = parseInt(count / 5) + 1;
                        }

                        findAPIFestivalNumPage = page;


                       for(var p=1; p<=page; p++){

                            $('#pageNum2').append(`<button class="pageBtn2 none">${p}</button>`)

                        }

                        $('#pageNum2').append(`<button class="pageBtn8">다음</button>`)
                        $('#pageNum2').append(`<button class="pageBtn9" value="20">마지막</button>`)

                        resolve();


                    }
                })

            })
        }


        /**
         * 전체 API리스트의 페이지 클릭 이벤트
         *
         */
        $(document).on('click','.pageBtn2', function(){

            let btns = document.querySelectorAll('.pageBtn2');
            btns.forEach(function(btn){
                btn.classList.remove('active');
            })

            $(this).addClass('active');
            const page = $(this).text();
            findAPIFestivalList(page);

        })

        /**
         * API리스트의 처음페이지 버튼 클릭 이벤트
         *
         */
        $(document).on('click','.pageBtn7', function(){

            current = 1;

            findAPIFestivalList(1)
            .then(()=>{
                // 1~10까지 보여줌
                return addShowingBtns(findAPIFestivalCount, 1, '.pageBtn2');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn2');
                btns.forEach(function(btn){
                    if(btn.textContent == "1"){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })
        })



        /**
         * API리스트의 마지막 페이지 버튼 클릭 이벤트
         *
         */
        $(document).on('click','.pageBtn9', function(){

            var num = 0;
            var lastPage = parseInt(findAPIFestivalNumPage);

            if(lastPage%10==0){
                num = (lastPage / 10);
            }else{
                num = parseInt((lastPage / 10) + 1);
            }

            current = num;

            console.log("num");
            console.log(num);



            findAPIFestivalList(lastPage)
                .then(()=>{
                return addShowingBtns(findAPIFestivalCount, num, '.pageBtn2');
                })
                .then(()=>{

                    let btns = document.querySelectorAll('.pageBtn2');
                    btns.forEach(function(btn){
                        if(btn.textContent == lastPage){
                            btn.classList.add('active');
                        }else{
                            btn.classList.remove('active');
                        }
                    })
                })
                .catch((error)=>{
                    alert("api 오류")
                })

        })



        /**
         * API리스트의 다음 페이지 버튼 클릭 이벤트
         *
         */

        $(document).on('click','.pageBtn8', function(){

            current = current + 1;
            var thisFirstPage = (current-1) * 10 + 1;

            findAPIFestivalList(thisFirstPage)
                .then(()=>{
                return addShowingBtns(findAPIFestivalCount, current, '.pageBtn2');
                })
                .then(()=>{

                    let btns = document.querySelectorAll('.pageBtn2');
                    btns.forEach(function(btn){
                        if(btn.textContent == thisFirstPage){
                            btn.classList.add('active');
                        }else{
                            btn.classList.remove('active');
                        }
                    })
                })
                .catch((error)=>{
                    alert("api 오류")
                })

        })







        // 처음 들어가면 1페이지 불러옴
        findAPIFestivalList(1)
            .then(()=>{
                // 처음 들어가면 api 페이지 버튼 생성
                addShowingBtns(findAPIFestivalCount, 1, '.pageBtn2');
            })
            .catch((error)=>{
                alert("api 오류")
            })





        /**
         * 체크된 API 리스트의 축제들을 DB에 저장함
         */
        $(document).on('click','#apiInsertBtn', function(){

            var checks = [];

            var selectedChecks = document.querySelectorAll("input[type='checkbox'].check2:checked");

            $.each(selectedChecks, function(index, item){


                checks.push($(item).val());

            })

            $.ajax({

                url: '/admin/festival-regulate/insertAPIFestivalList',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(checks),
                success: function(response){

                    alert(response);
                    setTimeout(function(){
                        window.location.href='/admin/festival-regulate';
                    },1000)


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

        var mulData = 0;


         /**
          * api 다중조건 검색 버튼 클릭
          */
         $(document).on('click','#searchBtn2', function(){

           var festivalName = $('#festivalName').val();
           var manageInstitution = $('#manageInstitution').val();
           var place = $('#place').val();
           var formattedStart = $('#formattedStart').val();
           var formattedEnd = $('#formattedEnd').val();

           var data = {

                   festivalName : festivalName,
                   manageInstitution : manageInstitution,
                   place : place,
                   formattedStart : formattedStart,
                   formattedEnd : formattedEnd
           };

           mulData = data;

            findAPIFestivalByMultiple(data,1);
            findAPIFestivalMultipleCount(data);
         })



          /**
           * API 수정 조건 폼 보내기
           * @param {Array<{name: string, value: string}>} data2 직렬화 된 폼 데이터
           * @param {int} page 페이지
           */
         function findAPIFestivalByMultiple(data2, page){

            return new Promise((resolve, reject)=>{

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
                                    <td class="timeId" title="${festival.timeId}">${festival.timeId}</td>
                                    <td class="festivalName" title="${festival.festivalName}">${festival.festivalName}</td>
                                    <td class="festivalContent" title="${festival.festivalContent}">${festival.festivalContent}</td>
                                    <td class="manageInstitution" title="${festival.manageInstitution}">${festival.manageInstitution}</td>
                                    <td class="tel" title="${festival.tel}">${festival.tel}</td>`;

                            var htmlContent12 =``;


                            if(! festival.homepageUrl || festival.homepageUrl == "null" || festival.homepageUrl == ""){

                                htmlContent12 = `

                                    <td class="homepageUrl"></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>
                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="startDate" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="endDate" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


                                `;

                            }else{

                                htmlContent12 = `

                                    <td class="homepageUrl" title="${festival.homepageUrl}"><a href="${festival.homepageUrl}">클릭!</a></td>
                                    <td class="detailAddress" title="${festival.detailAddress}">${festival.detailAddress}</td>
                                    <td class="place" title="${festival.place}">${festival.place}</td>
                                    <td class="startDate" title="${festival.formattedStart}">${festival.formattedStart}</td>
                                    <td class="endDate" title="${festival.formattedEnd}">${festival.formattedEnd}</td>
                                    <td class="season" title="${festival.season}">${festival.season}</td>


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

                        resolve();
                    }
                })
            })
         }


        var currentAPIMulNum = 1;
        var APIMulLastPage = 0;


         /**
        * API 상세검색 갯수 알아와서 페이지 버튼 갯수만큼 붙히기
        * @param {Array<{name: string, value: string}>} data2 직렬화 된 폼 데이터
        */
         function findAPIFestivalMultipleCount(data2){

            return new Promise((resolve, reject)=>{

                $.ajax({

                    url: '/admin/festival-regulate/findAPIFestivalMultipleCount',
                    method: 'POST',
                    data: JSON.stringify(data2),
                    contentType: 'application/json',
                    success: function(count){

                        $('#pageNum2').html("");
                        // 상세검색 전으로 돌아가기 버튼
                        $('#pageNum2').append(`<button class="pageBtn6">전체</button>`);
                        $('#pageNum2').append(`<button class="pageBtn16">처음</button>`);

                        console.log("카운트는...")
                        console.log(count)

                        var page = 0;

                        if(count % 5 ==0){
                            page = parseInt(count/5);
                        }else{
                            page = parseInt(count/5 +1);
                        }

                        APIMulLastPage = parseInt(page);

                        for(var p=1; p<=page; p++){

                            $('#pageNum2').append(`<button class="pageBtn5">${p}</button>`)

                        }

                        $('#pageNum2').append(`<button class="pageBtn17">다음</button>`);
                        $('#pageNum2').append(`<button class="pageBtn18">마지막</button>`);

                        resolve();

                    }
                })
            })
        }


      /**
       * api 상세검색 페이지 버튼에 해당 페이지의 순서에 해당하는 데이터 보이는 클릭 이벤트 추가
       */
        $(document).on('click','.pageBtn5', function(){

            let btns = document.querySelectorAll('.pageBtn5');

            btns.forEach(function(btn){

                btn.classList.remove('active');

            })

            $(this).addClass('active');
            const page = $(this).text();
            $('#list2').html("");
            findAPIFestivalByMultiple(mulData, page);

        })


        /**
       * 처음으로 돌아가기
       */
        $(document).on('click','.pageBtn16', function(){

            $('#list2').html("");
            $('#pageNum2').html("");
            currentAPIMulNum = 1;

            findAPIFestivalByMultiple(mulData, 1)
            .then(()=>{
                // 1~10까지 보여줌
                return addShowingBtns(findAPIFestivalMultipleCount, currentAPIMulNum, '.pageBtn5');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn5');
                btns.forEach(function(btn){
                    if(btn.textContent == "1"){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })



        /**
       * 마지막으로 가기
       */
        $(document).on('click','.pageBtn18', function(){
            $('#pageNum2').html("");
            $('#list2').html("");

            var num = 0;

            if(APIMulLastPage%10==0){
                num = (APIMulLastPage / 10);
            }else{
                num = parseInt((APIMulLastPage / 10) + 1);
            }

            currentAPIMulNum = num;

            findAPIFestivalByMultiple(mulData, APIMulLastPage)
            .then(()=>{
                return addShowingBtns(findAPIFestivalMultipleCount, currentAPIMulNum, '.pageBtn5');
            })
            .then(()=>{

                let btns = document.querySelectorAll('.pageBtn5');
                btns.forEach(function(btn){
                    if(btn.textContent == APIMulLastPage){
                        btn.classList.add('active');
                    }else{
                        btn.classList.remove('active');
                    }
                })
            })
            .catch((error)=>{
                alert("api 오류")
            })

        })


        /**
         * api 상세검색 리스트의 다음 페이지 버튼 클릭 이벤트
         *
         */

        $(document).on('click','.pageBtn17', function(){

            currentAPIMulNum = currentAPIMulNum + 1;
            var thisFirstPage = (currentAPIMulNum-1) * 10 + 1;

            findAPIFestivalByMultiple(mulData, thisFirstPage)
                .then(()=>{
                return addShowingBtns(findAPIFestivalMultipleCount, currentAPIMulNum, '.pageBtn5');
                })
                .then(()=>{

                    let btns = document.querySelectorAll('.pageBtn5');
                    btns.forEach(function(btn){
                        if(btn.textContent == thisFirstPage){
                            btn.classList.add('active');
                        }else{
                            btn.classList.remove('active');
                        }
                    })
                })
                .catch((error)=>{
                    alert("api 오류")
                })

        })




        /**
        * 전체 버튼으로 API 상세검색에서 API 전체리스트로 돌아가는 클릭 이벤트
        */
        $(document).on('click','.pageBtn6', function(){
            $('#pageNum2').html("");
            $('#list2').html("");
            console.log("pageBtn6");
            current = 1;
            findAPIFestivalList(1)
            .then(()=>{
                // 1~10까지 보여줌
                return addShowingBtns(findAPIFestivalCount, 1, '.pageBtn2');
            })
//            .then(()=>{
//
//                let btns = document.querySelectorAll('.pageBtn2');
//                btns.forEach(function(btn){
//                    if(btn.textContent == "1"){
//                        btn.classList.add('active');
//                    }else{
//                        btn.classList.remove('active');
//                    }
//                })
//            })
//            .catch((error)=>{
//                alert("api 오류")
//            })

        })



        /**
        * api 상세 조건 검색에서 초기화 버튼으로 돌아가기 버튼
        *
        */
        $(document).on('click','#resetBtn2', function(){

            document.getElementById('searchForm2').reset();

        })

        //#endregion




    }
)


