$(document).ready(function() {
    var currentPage = 1; // 초기 페이지는 1로 설정
    var totalPages = 0; // 초기에는 총 페이지 수를 알 수 없으므로 0으로 설정

    var currentPage2 = 1; // 초기 페이지 설정
    var totalPages2 = 1716; // 전체 페이지 수 (예시에서는 임의로 설정, 실제 데이터에 맞게 수정 필요)
    var itemsPerPage2 = 10; // 페이지당 표시할 항목 수
    var dbData = []; // DB 데이터를 저장할 배열
    var selectedCheckboxes = []; // 선택된 체크박스 인덱스를 저장할 배열



    function findListPage(currentPage, itemsPerPage){
    // AJAX를 통해 서버에서 전체 페이지 수(totalPages)를 받아오기
        $.ajax({
            type: "GET", // GET 방식으로 변경 (서버에서 페이지 수를 가져오는 것이므로)
            url: "/admin/cultural-properties-regulate/findListPage?page="+ currentPage + "&itemsPerPage=" + itemsPerPage,
            success: function(totalPages) {
                // 페이지네이션 초기화 및 렌더링
                $('#paginationSection1').empty();
                renderPagination1(currentPage, totalPages);

                console.log(itemsPerPage);
                console.log(totalPages);
            },
            error: function(xhr, status, error) {
                console.error('Error fetching pagination data: ' + error);
            }
        });
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
                renderPagination1(currentPage1, totalPages1); // 페이지 재렌더링

                getDBData(page);
            }
        });

        disableCheckboxes();
    }



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
                });


            },
            error: function(xhr, status, error) {
                console.error('API 호출 오류:', error);
            }
        });


    }



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
                        '<td class="registrationDate">' + formatDate(culturalProperties.registrationDate) + '</td>' +
                        '<td class="classifyA">' + culturalProperties.classifyA + '</td>' +
                        '<td class="classifyB">' + culturalProperties.classifyB + '</td>' +
                        '<td class="classifyC">' + culturalProperties.classifyC + '</td>' +
                        '<td class="classifyD">' + culturalProperties.classifyD + '</td>' +
                        '<td class="mainImgUrl"><img src="' + culturalProperties.mainImgUrl + '" style="max-width: 100px; max-height: 100px;"></td>';


                    // 날짜 포맷팅 함수 정의
                    function formatDate(dateString) {
                        var year = dateString.substring(0, 4);
                        var month = dateString.substring(4, 6);
                        var day = dateString.substring(6, 8);
                        return year + '-' + month + '-' + day;
                    }


                    // content 열 수정 시작
                    var content = culturalProperties.content;
                    var shortContent = content.substring(0, 20); // 처음 20글자만 추출
                    var fullContent = content; // 전체 내용

                        htmlContent +=
                            '<td class="content"><div class="content-tooltip" data-toggle="tooltip" title="' + fullContent + '">' + shortContent + '</div></td>';

                    // imgUrl 열 수정 시작
                        htmlContent += '<td class="imgUrl">';
                    if (Array.isArray(culturalProperties.imgUrl) && culturalProperties.imgUrl.length > 0) {
                        culturalProperties.imgUrl.forEach(function(url) {
                            htmlContent += '<a href="' + url + '" target="_blank" style="color: blue;">이미지</a><br>';
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
                                htmlContent += '<a href="' + url + '" target="_blank" style="color: blue;">동영상</a><br>';
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
                            htmlContent += '<a href="' + url + '" target="_blank" style="color: blue;">' + language + '</a><br>';

                        });
                    } else {
                        htmlContent += '-';
                    }

                    htmlContent +=  '<td><button class="btn btn-outline-primary" data-index="' + index + '">수정</button></td>';

                    var finalHtml = htmlCheck + htmlContent;
                    $('#list1').append(finalHtml);


                    // 툴팁 활성화
                    $(document).ready(function() {
                        $('[data-toggle="tooltip"]').tooltip();
                    });
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
        var pageIndex = parseInt($(this).attr('data-page'));
        currentPage2 = pageIndex; // 클릭된 페이지를 현재 페이지로 설정
        fetchApiData(pageIndex); // 데이터 불러오기

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





    function disableCheckboxes() {
        // 현재 페이지의 체크박스들을 모두 활성화 상태로 초기화
        $('#list2 .check2').prop('disabled', false).removeClass('disabled');

        // 현재 페이지의 각 체크박스에 대해 DB 데이터와 비교하여 비활성화 처리
        $('#list2 .check2').each(function() {
            var checkbox = $(this);
            var culturalPropertiesName = checkbox.closest('tr').find('.culturalPropertiesName').text().trim();

            // DB 데이터와 비교하여 해당 culturalPropertiesName이 DB에 이미 존재하는지 확인
            var existsInDB = dbData.some(function(item) {
                return item.culturalPropertiesName === culturalPropertiesName;
            });

            // DB에 존재하는 경우 체크 박스 비활성화 처리
            if (existsInDB) {
                checkbox.prop('disabled', true).addClass('disabled');
            }
        });
    }

    // 초기 페이지 데이터 불러오기
    fetchApiData(currentPage2);
    getDBData(1); // 초기 DB 데이터 불러오기
    findListPage(1, 10);


//    $(document).ready(function() {
//        $('#searchDB').click(function() {
//            // 사용자가 입력한 값을 가져오기
//            var category = $('#categoryFilter').val();
//            var name = $('#searchName').val();
//            var region = $('#searchRegion').val();
//            var dynasty = $('#searchDynasty').val();
//
//            // AJAX를 사용하여 서버로 데이터 전송
//            $.ajax({
//                url: '/admin/cultural-properties-regulate/search', // 실제 서버 API 엔드포인트 주소
//                method: 'GET', // HTTP 메서드
//                data: {
//                    category: category,
//                    name: name,
//                    region: region,
//                    dynasty: dynasty
//                },
//                success: function(response) {
//                    // 서버로부터 응답을 받았을 때 실행되는 함수
//                    console.log('검색 결과:', response);
//                    // 여기서 검색 결과를 화면에 표시하도록 추가적인 로직을 구현할 수 있습니다.
//                },
//                error: function(xhr, status, error) {
//                    // AJAX 요청이 실패했을 때 실행되는 함수
//                    console.error('AJAX 오류:', status, error);
//                }
//            });
//        });
//    });




//    $(document).ready(function() {
//        $('#searchDB').click(function(category, name, region, dynasty) {
//            // 사용자가 입력한 값을 가져오기
//            var category = $('#categoryFilter').val();
//            var name = $('#searchName').val();
//            var region = $('#searchRegion').val();
//            var dynasty = $('#searchDynasty').val();
//
//            if (category === 'all') {
//                // 전체를 선택한 경우 초기 화면을 표시
//                displayInitialScreen();
//            } else {
//                // AJAX를 사용하여 서버로 데이터 전송
//                $.ajax({
//                    url: '/admin/cultural-properties-regulate/search?category=' + category + '&name=' + name + '&region=' + region + '&dynasty=' + dynasty, // 서버 API 엔드포인트 주소
//                    method: 'POST', // HTTP 메서드
////                    data: {
////                        category: category,
////                        name: name,
////                        region: region,
////                        dynasty: dynasty
////                    },
//                    success: function(response) {
//                        // 서버로부터 응답을 받았을 때 실행되는 함수
//                        console.log('검색 결과:', response);
//
//                        // 검색 결과가 없으면 알림창을 띄우기
//                        if (response.length === 0) {
//                            alert('일치하는 검색 결과가 없습니다.');
//                        } else {
//                            // 검색 결과를 화면에 표시하도록 추가적인 로직을 구현
//                            renderData(response, 'list');
//                            // 페이징 데이터 렌더링 등 추가 처리
//                        }
//                    },
//                    error: function(xhr, status, error) {
//                        // AJAX 요청이 실패했을 때 실행되는 함수
//                        console.error('AJAX 오류:', status, error);
//                    }
//                });
//            }
//        });
//    });
//
//    // 초기 화면을 표시하는 함수
//    function displayInitialScreen() {
//        // 결과 영역을 비우기
//        $('#list').empty();
//
//        // 초기 화면에 보여줄 내용을 설정 (예: 기본 텍스트 또는 모든 데이터 로드)
//        $('#list').html('<div>초기 화면입니다. 검색어를 입력하거나 필터를 선택하세요.</div>');
//
//        // 추가적으로 초기화할 부분이 있으면 여기에 추가
//    }
//
//    // 데이터 렌더링 함수 예시
//    function renderData(data, targetElementId) {
//        var listContainer = $('#' + targetElementId);
//        listContainer.empty(); // 기존 내용 삭제
//
//        // 데이터가 비어있지 않다면
//        if (data.length > 0) {
//            data.forEach(function(item) {
//                listContainer.append('<div>' + item.cultural_properties_name + '</div>'); // 예시로 데이터 표시
//            });
//        } else {
//            listContainer.append('<div>일치하는 검색 결과가 없습니다.</div>');
//        }
//    }






//
//        function(category, name, region, dynasty, page)
//        function 새로운 함수(페이지 넣기) {
//
//
//
//        }
//
//
//
//
//         $(document).on('click','#searchDB', function(){
//
//            새로운 함수(1,...);
//
//         }
//
//
//
//        $('#searchDB').click(function(category, name, region, dynasty) {
//            // 사용자가 입력한 값을 가져오기
//            var category = $('#categoryFilter').val();
//            var name = $('#searchName').val();
//            var region = $('#searchRegion').val();
//            var dynasty = $('#searchDynasty').val();
//
//            if (category === 'all') {
//                // 전체를 선택한 경우 초기 화면을 표시
//                displayInitialScreen();
//            } else {
//                // AJAX를 사용하여 서버로 데이터 전송
//                $.ajax({
//                    url: '/admin/cultural-properties-regulate/search?category=' + category + '&name=' + name + '&region=' + region + '&dynasty=' + dynasty, // 서버 API 엔드포인트 주소
//                    method: 'POST', // HTTP 메서드
////                    data: {
////                        category: category,
////                        name: name,
////                        region: region,
////                        dynasty: dynasty
////                    },
//                    success: function(response) {
//                        // 서버로부터 응답을 받았을 때 실행되는 함수
//                        console.log('검색 결과:', response);
//
//                        // 클라이언트 측 데이터와 비교하여 일치하는 것을 처리하는 예시
//                        $('#list1').each(function() {
//                            var row = $(this);
//                            var culturalPropertiesName = row.find('.culturalPropertiesName').text().trim();
//
//                            // 검색 결과에 culturalPropertiesName이 포함되어 있는지 확인
//                            var existsInSearchResult = response.some(function(item) {
//                                return item.culturalPropertiesName === culturalPropertiesName;
//                            });
//
//                            // 일치하는 경우 행 보이기, 그렇지 않으면 숨기기
//                            if (existsInSearchResult) {
//                                row.show();
//                            } else {
//                                row.hide();
//                            }
//                        });
//
//                        // 검색 결과가 없으면 알림창을 띄우기
//                        if (response.length === 0) {
//                            alert('일치하는 검색 결과가 없습니다.');
//                        }
//                    },
//
//                    error: function(xhr, status, error) {
//                        // AJAX 요청이 실패했을 때 실행되는 함수
//                        console.error('AJAX 오류:', status, error);
//                    }
//                });
//            }
//
//
//            // 초기화 버튼 클릭 이벤트
//            $('#reset').click(function() {
//                // 검색 필터 초기화
//                $('#categoryFilter').val('all');
//                $('#searchName').val('');
//                $('#searchRegion').val('');
//                $('#searchDynasty').val('');
//
//                // 모든 행 보이기
//                $('#list1').show();
//            });
//
//
//        })









    // 검색 버튼 클릭 이벤트
    $('#searchDB').on('click', function() {
        // 사용자가 입력한 값을 가져오기
        var category = $('#categoryFilter').val();
        var name = $('#searchName').val();
        var region = $('#searchRegion').val();
        var dynasty = $('#searchDynasty').val();

        console.log(category);
        console.log(name);
        console.log(region);
        console.log(dynasty);

        searchDBCulturalProperties(category, name, region, dynasty, 1);
    });

    // 초기화 버튼 클릭 이벤트
    $('#reset').on('click', function() {
        // 검색 필터 초기화
        $('#categoryFilter').val('all');
        $('#searchName').val('');
        $('#searchRegion').val('');
        $('#searchDynasty').val('');

        // 모든 행 보이기
        $('#list1').show();
    });

    function searchDBCulturalProperties(category, name, region, dynasty, page) {
        if (category === 'all') {
            // 전체를 선택한 경우 초기 화면을 표시
            displayInitialScreen();
        } else {
            // AJAX를 사용하여 서버로 데이터 전송
            $.ajax({
                url: '/admin/cultural-properties-regulate/search?page=' + page + "&category=" + category + "&name=" + name +"&region=" + region +"&dynasty=" + dynasty,
                method: 'POST',
                data: {
                    categoryName: category,
                    culturalPropertiesName: name,
                    region: region,
                    dynasty: dynasty
                },
                success: function(response) {
                    // 서버로부터 응답을 받았을 때 실행되는 함수
                    console.log('검색 결과:', response);

                    // 클라이언트 측 데이터와 비교하여 일치하는 것을 처리하는 예시
                    $('#list1').each(function() {
                        var row = $(this);
                        var culturalPropertiesName = row.find('.culturalPropertiesName').text().trim();

                        // 검색 결과에 culturalPropertiesName이 포함되어 있는지 확인
                        var existsInSearchResult = response.some(function(item) {
                            return item.culturalPropertiesName === culturalPropertiesName;
                        });

                        // 일치하는 경우 행 보이기, 그렇지 않으면 숨기기
                        if (existsInSearchResult) {
                            row.show();
                        } else {
                            row.hide();
                        }
                    });

                    // 검색 결과가 없으면 알림창을 띄우기
                    if (response.length === 0) {
                        alert('일치하는 검색 결과가 없습니다.');
                    }
                },
                error: function(xhr, status, error) {
                    // AJAX 요청이 실패했을 때 실행되는 함수
                    console.error('AJAX 오류:', status, error);
                }
            });
        }
    }


    function displayInitialScreen() {
        // 결과 영역을 비우기
        $('#list1').empty();

        // 초기 화면에 보여줄 내용을 설정 (예: 기본 텍스트 또는 모든 데이터 로드)
        $('#list1').show();
//        $('#list1').html('<tr><td colspan="5">초기 화면입니다. 검색어를 입력하거나 필터를 선택하세요.</td></tr>');


    }



//    $(document).ready(function() {
//        $('#searchDB').click(function() {
//            // 사용자가 입력한 값을 가져오기
//            var categoryName = $('#categoryFilter').val();
//            var culturalPropertiesName = $('#searchName').val();
//            var region = $('#searchRegion').val();
//            var dynasty = $('#searchDynasty').val();
//
//            if (categoryName === 'all') {
//                // 전체를 선택한 경우 초기 화면을 표시
//                displayInitialScreen();
//            } else {
//                // AJAX를 사용하여 서버로 데이터 전송
//                $.ajax({
//                    url: '/admin/cultural-properties-regulate/search', // 서버 API 엔드포인트 주소
//                    method: 'GET', // HTTP 메서드
//                    data: {
//                        categoryName: categoryName,
//                        culturalPropertiesName: culturalPropertiesName,
//                        region: region,
//                        dynasty: dynasty
//                    },
//                    success: function(response) {
//                        console.log('검색 결과:', response);
//                        if (response.length === 0) {
//                            alert('일치하는 검색 결과가 없습니다.');
//                        } else {
//                            renderData(response, 'list');
//                        }
//                    },
//                    error: function(xhr, status, error) {
//                        console.error('AJAX 오류:', status, error);
//                    }
//                });
//            }
//        });
//
//        $('#reset').click(function() {
//            // 초기화 버튼 클릭 시 동작
//            $('#categoryFilter').val('all');
//            $('#searchName').val('');
//            $('#searchRegion').val('');
//            $('#searchDynasty').val('');
//            displayInitialScreen();
//        });
//    });
//
//    // 초기 화면을 표시하는 함수
//    function displayInitialScreen() {
//        // 결과 영역을 비우기
//        $('#list1').empty();
//
//        // 초기 화면에 보여줄 내용을 설정 (예: 기본 텍스트 또는 모든 데이터 로드)
//        $('#list1').html('<div>초기 화면입니다. 검색어를 입력하거나 필터를 선택하세요.</div>');
//
//        // 추가적으로 초기화할 부분이 있으면 여기에 추가
//    }


//    function searchDBData(searchTerm) {
//        var filteredData = dbData.filter(function(item) {
//            return item.culturalPropertiesName.includes(searchTerm) ||
//                   item.categoryName.includes(searchTerm) ||
//                   item.region.includes(searchTerm) ||
//                   item.dynasty.includes(searchTerm);
//        });
//
//        $('#list1').html("");
//        filteredData.forEach(function(culturalProperties, index) {
//            var htmlCheck = '<tr><td><input class="check1" type="checkbox" name="index" value="' + index + '"/></td>';
//            var htmlContent =
//                '<td class="id">' + culturalProperties.id + '</td>' +
//                '<td class="culturalPropertiesName">' + culturalProperties.culturalPropertiesName + '</td>' +
//                '<td class="categoryName">' + culturalProperties.categoryName + '</td>' +
//                '<td class="region">' + culturalProperties.region + '</td>' +
//                '<td class="dynasty">' + culturalProperties.dynasty + '</td>' +
//                '<td class="address">' + culturalProperties.address + '</td>' +
//                '<td class="registrationDate">' + formatDate(culturalProperties.registrationDate) + '</td>' +
//                '<td class="classifyA">' + culturalProperties.classifyA + '</td>' +
//                '<td class="classifyB">' + culturalProperties.classifyB + '</td>' +
//                '<td class="classifyC">' + culturalProperties.classifyC + '</td>' +
//                '<td class="classifyD">' + culturalProperties.classifyD + '</td>' +
//                '<td class="mainImgUrl"><img src="' + culturalProperties.mainImgUrl + '" style="max-width: 100px; max-height: 100px;"></td>';
//
//            function formatDate(dateString) {
//                var year = dateString.substring(0, 4);
//                var month = dateString.substring(4, 6);
//                var day = dateString.substring(6, 8);
//                return year + '-' + month + '-' + day;
//            }
//
//            var content = culturalProperties.content;
//            var shortContent = content.substring(0, 20);
//            var fullContent = content;
//
//            htmlContent +=
//                '<td class="content"><div class="content-tooltip" data-toggle="tooltip" title="' + fullContent + '">' + shortContent + '</div></td>';
//
//            htmlContent += '<td class="imgUrl">';
//            if (Array.isArray(culturalProperties.imgUrl) && culturalProperties.imgUrl.length > 0) {
//                culturalProperties.imgUrl.forEach(function(url) {
//                    htmlContent += '<a href="' + url + '" target="_blank" style="color: blue;">이미지</a><br>';
//                });
//            } else {
//                htmlContent += '-';
//            }
//            htmlContent += '</td>';
//
//            htmlContent += '<td class="imgDesc">';
//            if (Array.isArray(culturalProperties.imgDesc) && culturalProperties.imgDesc.length > 0) {
//                culturalProperties.imgDesc.forEach(function(desc) {
//                    htmlContent += desc + '<br>';
//                });
//            } else {
//                htmlContent += '-';
//            }
//            htmlContent += '</td>';
//
//            htmlContent += '<td class="videoUrl">';
//            if (Array.isArray(culturalProperties.videoUrl) && culturalProperties.videoUrl.length > 0) {
//                culturalProperties.videoUrl.forEach(function(url) {
//                    htmlContent += '<a href="' + url + '" target="_blank" style="color: blue;">비디오</a><br>';
//                });
//            } else {
//                htmlContent += '-';
//            }
//            htmlContent += '</td>';
//
//            $('#list1').append(htmlCheck + htmlContent);
//        });
//    }


//    $(document).ready(function() {
//        // 검색 버튼 클릭 이벤트
//        $('#searchDB').on('click', function() {
//            var category = $('#categoryFilter').val();
//            var name = $('#searchName').val().trim();
//            var region = $('#searchRegion').val().trim();
//            var dynasty = $('#searchDynasty').val().trim();
//
//            // 현재 목록에서 데이터 가져오기
//            var currentRows = $('#list1');
//
//            // 필터링된 데이터에 대한 배열 생성
//            var filteredRows = currentRows.filter(function() {
//                var row = $(this);
//                var id = row.find('.id').text();
//                var culturalPropertiesName = row.find('.culturalPropertiesName').text();
//                var categoryName = row.find('.categoryName').text();
//                var regionText = row.find('.region').text();
//                var dynastyText = row.find('.dynasty').text();
//
//                return (category === 'all' || categoryName === category) &&
//                       (name === '' || culturalPropertiesName.includes(name)) &&
//                       (region === '' || regionText.includes(region)) &&
//                       (dynasty === '' || dynastyText.includes(dynasty));
//            }); console.log(currentRows);
//
//            // 모든 행을 숨기고 필터링된 행만 보이게 하기
//            currentRows.hide();
//            if (filteredRows.length > 0) {
//                filteredRows.show();
//            } else {
//                // 일치하는 결과가 없을 때 알림창 띄우기
//                alert('일치하는 검색 결과가 존재하지 않습니다.');
//            }
//        });
//
//        // 초기화 버튼 클릭 이벤트
//        $('#reset').on('click', function() {
//            $('#categoryFilter').val('all');
//            $('#searchName').val('');
//            $('#searchRegion').val('');
//            $('#searchDynasty').val('');
//            $('#list1').show(); // 모든 행을 보이게 함
//        });
//    });







//    document.getElementById('searchDB').addEventListener('click', function() {
////    function search() {
//        var category = document.getElementById('categoryFilter').value;
//        var name = document.getElementById('searchName').value;
//        var region = document.getElementById('searchRegion').value;
//        var dynasty = document.getElementById('searchDynasty').value;
//
//        // 검색 조건에 따라 결과를 가공하여 출력
//        var results = [];
//
//        // 예시 결과 생성
//        results.push({ name: "국보 1", category: "국보", region: "서울", dynasty: "조선" });
//        results.push({ name: "보물 2", category: "보물", region: "경기", dynasty: "고려" });
//        results.push({ name: "국가무형유산 3", category: "국가무형유산", region: "부산", dynasty: "고구려" });
//
//        // 결과를 화면에 출력
//        var searchResultsElement = document.getElementById('searchResults');
//        if (!searchResultsElement) {
//            // 검색 결과를 표시할 요소가 없을 경우 알림창을 띄웁니다.
//            alert("검색 결과를 표시할 요소를 찾을 수 없습니다.");
//            return;
//        }
//        searchResultsElement.innerHTML = ''; // 결과 초기화
//
//        results.forEach(function(item) {
//            if ((category === 'all' || item.category === category) &&
//                (name === '' || item.name.includes(name)) &&
//                (region === '' || item.region.includes(region)) &&
//                (dynasty === '' || item.dynasty.includes(dynasty))) {
//
//                var resultElement = document.createElement('div');
//                resultElement.textContent = `이름: ${item.name}, 종목: ${item.category}, 지역: ${item.region}, 시대: ${item.dynasty}`;
//                searchResultsElement.appendChild(resultElement);
//            }
//        });
////    }
//    })
//    // 버튼 클릭 시 search() 함수 호출
////    document.getElementById('searchDB').addEventListener('click', search);



//    document.getElementById('searchDB').addEventListener('click', function() {
//        var category = document.getElementById('categoryFilter').value;
//        var name = document.getElementById('searchName').value.trim(); // 검색어 앞뒤 공백 제거
//        var region = document.getElementById('searchRegion').value.trim();
//        var dynasty = document.getElementById('searchDynasty').value.trim();
//
//        // Ajax로 가져온 데이터(list)를 사용하도록 가정
//        var filteredResults = dbData.filter(function(item) {
//            return (category === 'all' || item.categoryName === category) &&
//                   (name === '' || item.culturalPropertiesName.includes(name)) &&
//                   (region === '' || item.region.includes(region)) &&
//                   (dynasty === '' || item.dynasty.includes(dynasty));
//        });
//
//        var searchResultsElement = document.getElementById('searchResults');
//        if (!searchResultsElement) {
//            alert("검색 결과를 표시할 요소를 찾을 수 없습니다.");
//            return;
//        }
//
//        searchResultsElement.innerHTML = ''; // 결과 초기화
//
//        if (filteredResults.length === 0) {
//            var noResultElement = document.createElement('div');
//            noResultElement.textContent = '검색 결과가 없습니다.';
//            searchResultsElement.appendChild(noResultElement);
//        } else {
//            filteredResults.forEach(function(item) {
//                var resultElement = document.createElement('div');
//                resultElement.textContent = `이름: ${item.culturalPropertiesName}, 종목: ${item.categoryName}, 지역: ${item.region}, 시대: ${item.dynasty}`;
//                searchResultsElement.appendChild(resultElement);
//            });
//        }
//    });

//    $(document).ready(function() {
//        $('#searchDB').click(function() {
//            var category = $('#categoryFilter').val();
//            var name = $('#searchName').val().trim();
//            var region = $('#searchRegion').val().trim();
//            var dynasty = $('#searchDynasty').val().trim();
//
//            // 테이블 초기화
//            $('#list1').empty();
//
//            // 데이터 배열 예시 (실제 데이터는 여기서 하드코딩된 배열 대신에 서버에서 받거나 하드코딩된 객체로 대체할 수 있음)
//            var data = [
//                { id: 1, culturalPropertiesName: '문화재1', categoryName: '국보', region: '서울', dynasty: '조선' },
//                { id: 2, culturalPropertiesName: '문화재2', categoryName: '보물', region: '경기', dynasty: '고려' },
//                { id: 3, culturalPropertiesName: '문화재3', categoryName: '사적', region: '부산', dynasty: '신라' },
//                // 실제 데이터는 이 배열에 있어야 함
//            ];
//
//            // 입력된 조건에 따라 필터링
//            var filteredData = data.filter(function(item) {
//                return (category === 'all' || item.categoryName === category) &&
//                       (name === '' || item.culturalPropertiesName.includes(name)) &&
//                       (region === '' || item.region.includes(region)) &&
//                       (dynasty === '' || item.dynasty.includes(dynasty));
//            });
//
//            // 필터링된 결과를 테이블에 추가
//            filteredData.forEach(function(item) {
//                var row = '<tr>' +
//                    '<td><input class="check1" type="checkbox" name="index" value="' + item.id + '"/></td>' +
//                    '<td>' + item.id + '</td>' +
//                    '<td>' + item.culturalPropertiesName + '</td>' +
//                    '<td>' + item.categoryName + '</td>' +
//                    '<td>' + item.region + '</td>' +
//                    '<td>' + item.dynasty + '</td>' +
//                    '</tr>';
//                $('#list1').append(row);
//            });
//        });
//    });



//   $(document).ready(function() {
//       // 검색 옵션 패널 토글 기능
//       $('#toggleOptionsBtn').click(function() {
//           $('#searchOptions').toggle();
//       });
//
//       // 검색 버튼 클릭 시 검색 실행
//       $('#searchDB').click(function() {
//           filterTable();
//       });
//
//       // 필터링 함수 정의
//       function filterTable() {
//           var categoryFilter = $('#categoryFilter').val();
//           var searchName = $('#searchName').val().trim(); // 앞뒤 공백 제거
//           var searchRegion = $('#searchRegion').val().trim();
//           var searchDynasty = $('#searchDynasty').val().trim();
//
//           var found = false; // 검색 결과 유무를 나타내는 변수
//
//           $('#culturalPropertiesDB tbody tr').each(function() {
//               var rowVisible = true;
//
//               // 각 행의 데이터 가져오기
//               var name = $(this).find('td').eq(2).text().trim();
//               var category = $(this).find('td').eq(3).text().trim();
//               var region = $(this).find('td').eq(4).text().trim();
//               var dynasty = $(this).find('td').eq(5).text().trim();
//
//               // 필터링 조건에 따라 행을 숨기거나 보여주기
//               if (categoryFilter !== 'all' && category !== categoryFilter) {
//                   rowVisible = false;
//               }
//               if (searchName !== '' && !name.includes(searchName)) {
//                   rowVisible = false;
//               }
//               if (searchRegion !== '' && !region.includes(searchRegion)) {
//                   rowVisible = false;
//               }
//               if (searchDynasty !== '' && !dynasty.includes(searchDynasty)) {
//                   rowVisible = false;
//               }
//
//               // 행의 가시성 설정
//               if (rowVisible) {
//                   $(this).show();
//                   found = true; // 검색 결과가 있음을 표시
//               } else {
//                   $(this).hide();
//               }
//           });
//
//           // 검색 결과가 없을 경우 알림 표시
//           if (!found) {
//               alert('일치하는 검색 결과가 없습니다.');
//           }
//       }
//   });






});

