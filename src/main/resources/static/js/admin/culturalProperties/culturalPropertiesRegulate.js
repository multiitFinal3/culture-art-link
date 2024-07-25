        $(document).ready(function() {
//<!--            var currentPage1 = 1; // 문화재 DB 현황 초기 페이지 설정-->
//<!--            var totalPages1 = count; // 문화재 DB 현황 전체 페이지 수 (실제 데이터에 맞게 수정 필요)-->
//<!--            var itemsPerPage1 = 10;-->
//<!--            var checkedItems = {}; // 체크한 체크박스의 상태를 저장할 객체-->

            var currentPage = 1; // 초기 페이지는 1로 설정
            var totalPages = 0; // 초기에는 총 페이지 수를 알 수 없으므로 0으로 설정
//            var itemsPerPage = 10;
//            var itemsPerPage = $('#itemsPerPageSelect').val();

//            var currentPage = [[${currentPage}]];
//            var totalPages = [[${totalPages}]];

            var currentPage2 = 1; // 초기 페이지 설정
            var totalPages2 = 1716; // 전체 페이지 수 (예시에서는 임의로 설정, 실제 데이터에 맞게 수정 필요)
            var itemsPerPage2 = 10; // 페이지당 표시할 항목 수
            var dbData = []; // DB 데이터를 저장할 배열
            var selectedCheckboxes = []; // 선택된 체크박스 인덱스를 저장할 배열

           // 페이지 로딩 시 초기화
//<!--            renderPagination1();-->
//
//<!--            function renderPagination1() {-->
//<!--                $('#paginationSection1').empty(); // 기존 버튼 제거-->
//
//<!--                // 이전 버튼-->
//<!--                var prevHtml = '<li class="page-item ' + (currentPage1 == 1 ? 'disabled' : '') + '">' +-->
//<!--                    '<a class="page-link prev-link1" href="#" data-page="' + (currentPage1 - 1) + '">이전</a>' +-->
//<!--                    '</li>';-->
//<!--                $('#paginationSection1').append(prevHtml);-->
//
//<!--                // 페이지 번호 버튼-->
//<!--                var startPage = (Math.ceil(currentPage1 / itemsPerPage1) - 1) * itemsPerPage1 + 1;-->
//<!--                var endPage = Math.min(startPage + itemsPerPage1 - 1, totalPages1);-->
//
//<!--                for (var pageNum = startPage; pageNum <= endPage; pageNum++) {-->
//<!--                    var pageHtml = '<li class="page-item ' + (pageNum == currentPage1 ? 'active' : '') + '">' +-->
//<!--                        '<a class="page-link page-num1" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +-->
//<!--                        '</li>';-->
//<!--                    $('#paginationSection1').append(pageHtml);-->
//<!--                }-->
//
//<!--                // 다음 버튼-->
//<!--                var nextHtml = '<li class="page-item ' + (currentPage1 >= totalPages1 ? 'disabled' : '') + '">' +-->
//<!--                    '<a class="page-link next-link1" href="#" data-page="' + (currentPage1 + 1) + '">다음</a>' +-->
//<!--                    '</li>';-->
//<!--                $('#paginationSection1').append(nextHtml);-->
//
//<!--                // 페이지 번호 클릭 이벤트 핸들러-->
//<!--                $('.page-num1').click(function(e) {-->
//<!--                    e.preventDefault();-->
//<!--                    var page = $(this).data('page');-->
//<!--                    currentPage1 = page;-->
//<!--                    renderPagination1(); // 페이지네이션 다시 렌더링-->
//<!--                    // loadPageData(page); // 필요하다면 페이지 데이터 로드 함수 호출-->
//<!--                });-->
//
//<!--                // 이전 버튼 클릭 이벤트 핸들러-->
//<!--                $('.prev-link1').click(function(e) {-->
//<!--                    e.preventDefault();-->
//<!--                    if (currentPage1 > 1) {-->
//<!--                        currentPage1--;-->
//<!--                        renderPagination1(); // 페이지네이션 다시 렌더링-->
//<!--                        // loadPageData(currentPage1); // 필요하다면 페이지 데이터 로드 함수 호출-->
//<!--                    }-->
//<!--                });-->
//
//<!--                // 다음 버튼 클릭 이벤트 핸들러-->
//<!--                $('.next-link1').click(function(e) {-->
//<!--                    e.preventDefault();-->
//<!--                    if (currentPage1 < totalPages1) {-->
//<!--                        currentPage1++;-->
//<!--                        renderPagination1(); // 페이지네이션 다시 렌더링-->
//<!--                        // loadPageData(currentPage1); // 필요하다면 페이지 데이터 로드 함수 호출-->
//<!--                    }-->
//<!--                });-->
//<!--            }-->
//totalCount
//<!--            // AJAX를 사용해 서버에서 count 값을 받아오기-->
//<!--            $.ajax({-->
//<!--                type: "POST",-->
//<!--                url: "/admin/cultural-properties-regulate/select/count",-->
//<!--                success: function(count) {-->
//<!--                    totalPages1 = response; // 서버에서 받아온 count 값을 totalPages1 변수에 할당-->
//<!--                    renderPagination1(); // 페이지네이션 초기화-->
//<!--                    int page = 0;-->
//<!--                    if(count % 10 == 0) {-->
//<!--                        page = count / 10;-->
//<!--                    }else{-->
//<!--                        page = count / 10 + 1;-->
//<!--                    }-->
//<!--                    renderPagination1(page);-->
//<!--                },-->
//<!--                error: function() {-->
//<!--                    alert("데이터를 불러오는 중 오류가 발생했습니다.");-->
//<!--                }-->
//<!--            });-->

//            // renderPagination1 함수
//            function renderPagination1() {
//                $('#paginationSection1').empty(); // 기존 버튼 제거
//
//                // 이전 버튼 생성
//                    var prevHtml = '<li class="page-item ' + (currentPage1 == 1 ? 'disabled' : '') + '">' +
//                        '<a class="page-link prev-link" href="#" data-page="' + (currentPage1 - 1) + '">이전</a>' +
//                        '</li>';
//                    $('#paginationSection1').append(prevHtml);
//
//                    // 페이지 번호 버튼 생성
//                    var startPage = Math.floor((currentPage1 - 1) / 10) * 10 + 1;
//                    var endPage = Math.min(startPage + 9, totalPages1); // totalPages1은 서버에서 받아온 전체 페이지 수
//
//                    for (var pageNum = startPage; pageNum <= endPage; pageNum++) {
//                        var pageHtml = '<li class="page-item ' + (pageNum == currentPage1 ? 'active' : '') + '">' +
//                            '<a class="page-link page-num" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
//                            '</li>';
//                        $('#paginationSection1').append(pageHtml);
//                    }
//
//                    // 다음 버튼 생성
//                    var nextHtml = '<li class="page-item ' + (currentPage1 >= totalPages1 ? 'disabled' : '') + '">' +
//                        '<a class="page-link next-link" href="#" data-page="' + (currentPage1 + 1) + '">다음</a>' +
//                        '</li>';
//                    $('#paginationSection1').append(nextHtml);
//
//                    // 페이지 번호 클릭 이벤트 처리
//                    $('.page-link').on('click', function(e) {
//                        e.preventDefault();
//                        var page = $(this).data('page');
//                        if (page !== currentPage1) {
//                            currentPage1 = page;
//                            renderPagination1(); // 페이지 재렌더링
//                            getDBData(); // 데이터 가져오는 함수 (실제로 필요에 맞게 수정 필요)
//                        }
//                    });
//
//                    // 데이터가 있는 페이지의 체크박스 비활성화 처리
//                    disableCheckboxes(); // 실제 기능에 맞게 수정 필요
//                }

            function findListPage(currentPage, itemsPerPage){
            // AJAX를 통해 서버에서 전체 페이지 수(totalPages)를 받아오기
                $.ajax({
                    type: "GET", // GET 방식으로 변경 (서버에서 페이지 수를 가져오는 것이므로)
//                    url: "/admin/cultural-properties-regulate/findListPage?itemsPerPage=" + itemsPerPage,
                    url: "/admin/cultural-properties-regulate/findListPage?page="+ currentPage + "&itemsPerPage=" + itemsPerPage,
                    success: function(totalPages) {
                        // 페이지네이션 초기화 및 렌더링
                        $('#paginationSection1').empty();
                        renderPagination1(currentPage, totalPages);
    //                    success: function(response) {
    //                        var totalPages = response;
    //                        totalPages = response; // 서버에서 받아온 전체 페이지 수를 totalPages1 변수에 할당
        //                    renderPagination1(); // 페이지네이션 초기화


                        console.log(itemsPerPage);
                        console.log(totalPages);


//                        // 이전 페이지 링크
//                        var prevDisabled = (currentPage === 1 || totalPages === 0) ? 'disabled' : '';
//                        var prevLink = (currentPage > 1) ? (currentPage - 1) : currentPage;
//                        var prevHtml = '<li class="page-item ' + prevDisabled + '"><a class="page-link" href="#" onclick="changePage(' + prevLink + ')">이전</a></li>';
//                        $('#paginationSection1').append(prevHtml);
//
//                        // 페이지 번호 링크
//                        for (var i = 1; i <= totalPages; i++) {
//                            var activeClass = (currentPage === i) ? 'active' : '';
//                            var pageHtml = '<li class="page-item ' + activeClass + '"><a class="page-link" href="#" onclick="changePage(' + i + ')">' + i + '</a></li>';
//                            $('#paginationSection1').append(pageHtml);
//                        }
//
//                        // 다음 페이지 링크
//                        var nextDisabled = (currentPage === totalPages || totalPages === 0) ? 'disabled' : '';
//                        var nextLink = (currentPage < totalPages) ? (currentPage + 1) : currentPage;
//                        var nextHtml = '<li class="page-item ' + nextDisabled + '"><a class="page-link" href="#" onclick="changePage(' + nextLink + ')">다음</a></li>';
//                        $('#paginationSection1').append(nextHtml);

                        // 데이터 불러오기
                        getDBData(currentPage, itemsPerPage);
                    },
                    error: function(xhr, status, error) {
                        console.error('Error fetching pagination data: ' + error);
                    }


            });

                // 데이터가 있는 페이지의 체크박스 비활성화 처리
//                disableCheckboxes(); // 실제 기능에 맞게 수정 필요
//            }

                }


                // renderPagination1 함수
                function renderPagination1(currentPage1, totalPages1) {
                    $('#paginationSection1').empty(); // 기존 버튼 제거

                    // 이전 버튼
                    var prevHtml = '<li class="page-item ' + (currentPage1 == 1 ? 'disabled' : '') + '">' +
                        '<a class="page-link prev-link" href="#" data-page="' + (currentPage1 - 1) + '">이전</a>' +
                        '</li>';
                    $('#paginationSection1').append(prevHtml);

                    // 페이지 번호 버튼
                    var startPage = Math.floor((currentPage1 - 1) / 10) * 10 + 1;
                    var endPage = Math.min(startPage + 9, totalPages1);

                    for (var pageNum = startPage; pageNum <= endPage; pageNum++) {
                        var pageHtml = '<li class="page-item ' + (pageNum == currentPage1 ? 'active' : '') + '">' +
                            '<a class="page-link page-num" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
                            '</li>';
                        $('#paginationSection1').append(pageHtml);
                    }

                    // 다음 버튼
                    var nextHtml = '<li class="page-item ' + (currentPage1 >= totalPages1 ? 'disabled' : '') + '">' +
                        '<a class="page-link next-link" href="#" data-page="' + (currentPage1 + 1) + '">다음</a>' +
                        '</li>';
                    $('#paginationSection1').append(nextHtml);

                    // 페이지 번호 클릭 이벤트 처리
                    $('.page-link').on('click', function(e) {
                        e.preventDefault();
                        var page = $(this).data('page');
                        if (page !== currentPage1) {
                            currentPage1 = page;
                            renderPagination1(1, totalPages1); // 페이지 재렌더링
                            getDBData(); // 데이터 가져오는 예시 함수 (실제로 필요에 맞게 수정 필요)
                        }
                    });

                    // 데이터가 있는 페이지의 체크박스 비활성화 처리
                    disableCheckboxes(); // 실제 기능에 맞게 수정 필요
                }

//                function renderPagination(totalPages) {
//                    // 페이지네이션 초기화
//                    $('#paginationSection').empty();
//
//                    // 페이지 번호 링크 추가
//                    for (var i = 1; i <= totalPages; i++) {
//                        var pageHtml = '<li class="page-item"><a class="page-link" href="#" onclick="fetchAndRenderPage(' + i + ')">' + i + '</a></li>';
//                        $('#paginationSection').append(pageHtml);
//                    }
//                }




            // 페이지 버튼 생성 함수
            function renderPagination2() {
                $('#paginationSection2').empty(); // 기존 버튼 제거

                // 이전 버튼
                var prevHtml = '<li class="page-item ' + (currentPage2 == 1 ? 'disabled' : '') + '">' +
                    '<a class="page-link prev-link" href="#" data-page="' + (currentPage2 - 1) + '">이전</a>' +
                    '</li>';
                $('#paginationSection2').append(prevHtml);

                // 페이지 번호 버튼
                var startPage = (Math.ceil(currentPage2 / 10) - 1) * 10 + 1;
                var endPage = Math.min(startPage + 9, totalPages2);

                for (var pageNum = startPage; pageNum <= endPage; pageNum++) {
                    var pageHtml = '<li class="page-item ' + (pageNum == currentPage2 ? 'active' : '') + '">' +
                        '<a class="page-link page-num" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
                        '</li>';
                    $('#paginationSection2').append(pageHtml);
                }

                // 다음 버튼
                var nextHtml = '<li class="page-item ' + (currentPage2 >= totalPages2 ? 'disabled' : '') + '">' +
                    '<a class="page-link next-link" href="#" data-page="' + (currentPage2 + 1) + '">다음</a>' +
                    '</li>';
                $('#paginationSection2').append(nextHtml);

                // 데이터가 있는 페이지의 체크박스 비활성화 처리
                disableCheckboxes();
            }

            // Ajax를 통해 API 데이터 불러오기
            function fetchApiData(pageIndex) {
                $('#list2').html(""); // 테이블 내용 초기화
                $('#loadingIndicator').show(); // 로딩 표시

                $.ajax({
                    url: '/admin/cultural-properties-regulate/fetchApiData?pageIndex=' + pageIndex,
                    method: 'POST',
                    contentType: 'application/json',
                    beforeSend: function() {
                        $('#loadingIndicator').show(); // Ajax 요청 전에 로딩 표시 보이기
                    },
                    success: function(list) {
                        $.each(list, function(index, culturalProperties) {
                            var index1 = (pageIndex - 1) * itemsPerPage2 + index + 1;

                            var htmlCheck = '<tr><td><input class="check2" type="checkbox" name="index" value="' + index + '"/></td>';
                            var htmlContent =
                                '<td class="culturalPropertieId">' + index1 + '</td>' +
                                '<td class="culturalPropertiesName">' + culturalProperties.culturalPropertiesName + '</td>' +
                                '<td class="categoryName">' + culturalProperties.categoryName + '</td>' +
                                '<td class="region">' + culturalProperties.region + '</td>' +
                                '<td class="dynasty">' + culturalProperties.dynasty + '</td></tr>';

                            var finalHtml = htmlCheck + htmlContent;
                            $('#list2').append(finalHtml);
                        });

                        // 페이지네이션 다시 그리기
                        renderPagination2();


                        // 데이터를 모두 표시한 후 로딩 인디케이터 숨기기
                        setTimeout(function() {
                            $('#loadingIndicator').hide();
                        }); // 0.5초 후에 숨김 (필요에 따라 조정 가능)


                    },
                    error: function(xhr, status, error) {
                        console.error('API 호출 오류:', error);
                    }
                });


            }




//             //
//            function getDBListCount(itemsPerPage){
//
//                $('#list1').html(""); // 테이블 내용 초기화
//
//                $.ajax({
//                    url: '/admin/cultural-properties-regulate/findListPage?itemsPerPage=' + itemsPerPage,
//                    method: 'GET',
//                    contentType: 'application/json',
//                    success: function(page) {
//
//                        console.log("page : ")
//                        console.log(page)
//
//                        $('#paginationSection1').html("");
//
//                        for(var p=1; p<=page; p++){
//
//                            $('#paginationSection1').append(`<button>${p}</button>`);
//
//                        }
//
//
//                    }
//
//
//                })
//
//            }

//            getDBListCount(count);







//<!--// AJAX를 통해 서버에서 페이지 수를 받아오고 totalPages1 변수 업데이트-->
//<!--function getDBListCount(itemsPerPage) {-->
//<!--    $('#list1').html(""); // 테이블 내용 초기화-->
//
//<!--    $.ajax({-->
//<!--        url: '/admin/cultural-properties-regulate/findListPage?itemsPerPage=' + itemsPerPage,-->
//<!--        method: 'GET',-->
//<!--        contentType: 'application/json',-->
//<!--        success: function(totalPages) {-->
//<!--            console.log("Total pages: " + totalPages);-->
//
//<!--            // totalPages1 업데이트-->
//<!--            totalPages1 = totalPages;-->
//
//<!--            // renderPagination1 함수 호출하여 페이지 버튼 생성-->
//<!--            renderPagination1();-->
//
//<!--            // 초기 데이터 로드-->
//<!--            getDBData(currentPage1, itemsPerPage);-->
//<!--        },-->
//<!--        error: function(xhr, status, error) {-->
//<!--            console.error('페이지 수 요청 중 오류 발생:', error);-->
//<!--        }-->
//<!--    });-->
//<!--}-->
//
//<!--// 초기 페이지 로드-->
//<!--$(document).ready(function() {-->
//<!--    // 초기 데이터 로드 및 페이지 버튼 생성-->
//<!--    var itemsPerPage = 10; // 페이지당 아이템 수-->
//<!--    getDBListCount(itemsPerPage);-->
//<!--});-->


            // Ajax를 통해 DB 데이터 불러오기
            function getDBData(page) {
                $('#list1').html(""); // 테이블 내용 초기화

                $.ajax({
                    url: '/admin/cultural-properties-regulate/select?page='+ page,
                    method: 'GET',
                    contentType: 'application/json',
                    success: function(list) {

                        dbData = list; // DB 데이터를 배열에 저장

                        // DB 데이터를 통해 API 데이터와 비교하여 체크 박스 비활성화
                        disableCheckboxes();

                        $.each(list, function(index, culturalProperties) {
                            var htmlCheck = '<tr><td><input class="check1" type="checkbox" name="index" value="' + index + '"/></td>';
                            var htmlContent =
                                '<td class="id">' + culturalProperties.id + '</td>' +
                                '<td class="culturalPropertiesName">' + culturalProperties.culturalPropertiesName + '</td>' +
                                '<td class="categoryName">' + culturalProperties.categoryName + '</td>' +
                                '<td class="region">' + culturalProperties.region + '</td>' +
                                '<td class="dynasty">' + culturalProperties.dynasty + '</td>' +
                                '<td class="address">' + culturalProperties.address + '</td>' +
                                '<td class="registrationDate">' + culturalProperties.registrationDate + '</td>' +
                                '<td class="classifyA">' + culturalProperties.classifyA + '</td>' +
                                '<td class="classifyB">' + culturalProperties.classifyB + '</td>' +
                                '<td class="classifyC">' + culturalProperties.classifyC + '</td>' +
                                '<td class="classifyD">' + culturalProperties.classifyD + '</td>' +
                                '<td class="mainImgUrl"><a href="' + culturalProperties.mainImgUrl + '" target="_blank">클릭!</a></td>';

                            // content 열 수정 시작
                            var content = culturalProperties.content;
                            var shortContent = content.substring(0, 20); // 처음 5글자만 추출
                            var fullContent = content; // 전체 내용

                            htmlContent +=
                                '<td class="content" onmouseover="this.innerHTML=\'' + fullContent + '\'" onmouseout="this.innerHTML=\'' + shortContent + '\'">' + shortContent + '</td>';


                            // imgUrl 열 수정 시작
                            htmlContent += '<td class="imgUrl">';
                            if (Array.isArray(culturalProperties.imgUrl) && culturalProperties.imgUrl.length > 0) {
                                culturalProperties.imgUrl.forEach(function(url) {
                                    htmlContent += '<a href="' + url + '" target="_blank">클릭!</a><br>';
                                });
                            } else {
                                htmlContent += '-';
                            }
                            htmlContent += '</td>';


                            // imgDesc 열 수정 시작
                            htmlContent += '<td class="imgDesc">';
                            if (Array.isArray(culturalProperties.imgDesc) && culturalProperties.imgDesc.length > 0) {
                                culturalProperties.imgDesc.forEach(function(desc) {
                                    htmlContent += desc + '<br>';
                                });
                            } else {
                                htmlContent += '-';
                            }
                            htmlContent += '</td>';
                            // imgDesc 열 수정 끝

                            htmlContent +=


                                '<td class="videoUrl">';
                            if (Array.isArray(culturalProperties.videoUrl) && culturalProperties.videoUrl.length > 0) {
                                culturalProperties.videoUrl.forEach(function(url) {
                                    if (!url || url === 'http://116.67.83.213/webdata/file_data/media_data/videos/') {
                                        htmlContent += '데이터가 존재하지 않습니다<br>';
                                    } else {
                                        htmlContent += '<a href="' + url + '" target="_blank">클릭!</a><br>';
                                    }
                                });
                            } else {
                                if (!culturalProperties.videoUrl || culturalProperties.videoUrl === 'http://116.67.83.213/webdata/file_data/media_data/videos/') {
                                    htmlContent += '데이터가 존재하지 않습니다<br>';
                                } else {
                                    htmlContent += '-';
                                }
                            }
                            htmlContent += '</td>' +
                                '<td class="narrationUrl">';

                            // narrationUrl이 배열인 경우 각 URL에 대해 링크 생성
                            if (Array.isArray(culturalProperties.narrationUrl) && culturalProperties.narrationUrl.length > 0) {
                                culturalProperties.narrationUrl.forEach(function(url, index) {
                                    var language = '';
                                    switch (index) {
                                        case 0:
                                            language = '한국어';
                                            break;
                                        case 1:
                                            language = '영어';
                                            break;
                                        case 2:
                                            language = '일본어';
                                            break;
                                        case 3:
                                            language = '중국어';
                                            break;
                                        default:
                                            language = '언어 ' + (index + 1); // 기본적으로 언어 1, 2, 3, ...
                                            break;
                                    }
                                    htmlContent += '<a href="' + url + '" target="_blank">' + language + '</a><br>';
                                });
                            } else {
                                htmlContent += '-';
                            }
                            htmlContent += '</td></tr>';

                            var finalHtml = htmlCheck + htmlContent;
                            $('#list1').append(finalHtml);
                        });



                    },
                    error: function(xhr, status, error) {
                        console.error('DB 데이터 호출 오류:', error);
                    }
                });
            }


            // 페이지 버튼 클릭 이벤트
            $(document).on('click', '.page-num, .prev-link, .next-link', function(e) {
                e.preventDefault();
                var page = parseInt($(this).attr('data-page'));
                currentPage1 = page; // 클릭된 페이지를 현재 페이지로 설정
                getDBData(page); // 데이터 불러오기

//<!--                applyCheckboxState(); // 체크박스 상태 적용-->
            });


            // 페이지 버튼 클릭 이벤트
            $(document).on('click', '.page-num, .prev-link, .next-link', function(e) {
                e.preventDefault();
                var pageIndex = parseInt($(this).attr('data-page'));
                currentPage2 = pageIndex; // 클릭된 페이지를 현재 페이지로 설정
                fetchApiData(pageIndex); // 데이터 불러오기

//<!--                applyCheckboxState(); // 체크박스 상태 적용-->
            });

            // 전체 선택 체크박스 클릭 이벤트 (DB 현황)
            $('#selectAllCheckDB').click(function() {
                var isChecked = $(this).prop('checked');
                $('#culturalPropertiesDB').find('.check1').prop('checked', isChecked);
            });

            // 전체 선택 체크박스 클릭 이벤트 (API 데이터)
            $('#selectAllCheckAPI').click(function() {
                var isChecked = $(this).prop('checked');
                $('#culturalPropertiesAPI').find('.check2').prop('checked', isChecked);
            });

            // "DB 추가" 버튼 클릭 이벤트 핸들러
            $('#addDB').click(function() {
                // 체크된 데이터 가져오기
                var checkedItem = [];
                $('.check2:checked').each(function() {

                    var index = $(this).val(); // 선택된 인덱스(데이터 식별자) 가져오기
                    console.log("$('.check2:checked').each(function()");
                    console.log(index);
                    checkedItem.push(index); // 선택된 데이터를 배열에 추가
                });

                console.log("체크된 내용");
                console.log(checkedItem);

                // 선택된 데이터를 서버로 전송
                $.ajax({
                    url: '/admin/cultural-properties-regulate/addDBData',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(checkedItem), // 선택된 데이터를 JSON 형태로 전송
                    beforeSend: function() {
                        // Ajax 요청 전에 로딩 인디케이터를 보여주도록 설정
                        $('#loadingIndicator').show();
                    },
                    success: function(response) {
                        alert('데이터베이스에 성공적으로 추가되었습니다.');
                        // 추가된 데이터를 문화재 DB 현황 테이블에 업데이트
                        getDBData(1); // 데이터베이스 데이터 다시 불러오기


                        // 체크박스 비활성화 처리
                        $('.check2:checked').prop('disabled', true);

                        $('#loadingIndicator').hide();
                    }
                });
            });


            // DB 데이터와 비교하여 체크 박스 비활성화 함수
            function disableCheckboxes() {
                // 현재 페이지의 체크박스들을 모두 활성화 상태로 초기화
                $('#list2 .check2').prop('disabled', false).removeClass('disabled');

                // DB 데이터와 현재 페이지의 데이터를 비교하여 이미 DB에 존재하는 경우 해당 체크 박스 비활성화 처리
                for (var i = 0; i < itemsPerPage2; i++) {
                    var dataIndex = (currentPage2 - 1) * itemsPerPage2 + i;
                    var checkbox = $('#list2').find('.check2').eq(dataIndex);

                    // 해당 데이터가 DB에 이미 존재하는지 확인
                    var culturalPropertiesName = checkbox.closest('tr').find('.culturalPropertiesName').text().trim();
                    var existsInDB = dbData.some(function(item) {
                        return item.culturalPropertiesName === culturalPropertiesName;
                    });

                    // DB에 존재하는 경우 체크 박스 비활성화 처리
                    if (existsInDB) {
                        checkbox.prop('disabled', true).addClass('disabled');
                    }
                }
            }



            // 초기 페이지 데이터 불러오기
            fetchApiData(currentPage2);
            getDBData(1); // 초기 DB 데이터 불러오기
            findListPage(1, 10);


        });

























            //
//            function getDBListCount(itemsPerPage){
//
//                $('#list1').html(""); // 테이블 내용 초기화
//
//                $.ajax({
//                    url: '/admin/cultural-properties-regulate/findListPage?itemsPerPage=' + itemsPerPage,
//                    method: 'GET',
//                    contentType: 'application/json',
//                    success: function(page) {
//
//                        console.log("page : ")
//                        console.log(page)
//
//                        $('#list33').html("");
//
//                        for(var p=1; p<=page; p++){
//
//                            $('#list33').append(`<button>${p}</button>`);
//
//                        }
//
//
//                    }
//
//
//                })
//
//            }
//
//            getDBListCount(10);
