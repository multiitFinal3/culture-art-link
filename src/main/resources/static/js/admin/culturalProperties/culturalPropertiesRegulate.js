$(document).ready(function() {
    var currentPage = 1; // 초기 페이지는 1로 설정
    var totalPages = 0; // 초기에는 총 페이지 수를 알 수 없으므로 0으로 설정

    var currentPage2 = 1; // 초기 페이지 설정
    var totalPages2 = 1716; // 전체 페이지 수 (예시에서는 임의로 설정, 실제 데이터에 맞게 수정 필요)
    var itemsPerPage2 = 10; // 페이지당 표시할 항목 수
    var dbData = []; // DB 데이터를 저장할 배열
    var totalDBData = [];
    var selectedCheckboxes = []; // 선택된 체크박스 인덱스를 저장할 배열


    var itemsPerPage = 10; // 페이지당 항목 수

     function findtotalDBData(){

        $.ajax({
            type: "POST",
            url: "/admin/cultural-properties-regulate/findtotalDBData",
            success: function(list) {
                console.log("findtotalDBData");
                console.log(list);
                totalDBData = list;


            }
        })
    }

    findtotalDBData();


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
                '<a class="page-link page-num1" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
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
                '<a class="page-link page-num2" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
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

                    htmlContent +=  '<td><button class="btn btn-outline-primary" id="updateDB" data-index="' + index + '">수정</button></td>';

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
    $(document).on('click', '.page-num2, .prev-link, .next-link', function(e) {
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
            var existsInDB = totalDBData.some(function(item) {
                return item.culturalPropertiesName === culturalPropertiesName;
            });

            // DB에 존재하는 경우 체크 박스 비활성화 처리
            if (existsInDB) {
                checkbox.prop('disabled', true).addClass('disabled');
            }
        });
    }



    $('#deleteDB').on('click', function() {
        // 체크된 항목의 ID 수집
        var checkedIds = [];
        // 각 체크박스가 속한 행의 ID를 가져옴
        $('.check1:checked').each(function() {
            // 체크박스가 속한 <tr> 요소에서 ID 값을 찾음
            var row = $(this).closest('tr'); // 현재 체크박스의 부모 tr을 찾음
            var id = row.find('.id').text(); // .id 클래스를 가진 td에서 ID 값 추출
            checkedIds.push(parseInt(id)); // ID 값을 배열에 추가
        });

        // 체크된 항목이 없으면 경고 메시지 표시
        if (checkedIds.length === 0) {
            alert('삭제할 항목을 선택해 주세요.');
            return;
        }

        // 확인 메시지
        if (confirm('선택한 항목을 정말 삭제하시겠습니까?')) {
            // AJAX 요청
            $.ajax({
                url: '/admin/cultural-properties-regulate/deleteDBData',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(checkedIds), // 체크된 ID 배열을 JSON으로 변환하여 전송
                success: function(response) {
                    alert('삭제 성공!'); // 성공 메시지 표시
                    location.reload(); // 페이지 새로 고침
                },
                error: function(xhr, status, error) {
                    console.error('삭제 오류:', error);
                    alert('삭제 중 오류가 발생했습니다. 다시 시도해 주세요.');
                }
            });
        }
    });


    // 수정 버튼 클릭 이벤트 핸들러
    $('#list1').on('click', '#updateDB', function () {
        showEditRow($(this)); // 수정 행 표시
    });

    // 수정 행 표시 함수
    function showEditRow($button) {
    console.log($button); // 버튼 확인
        var $row = $button.closest('tr'); // 클릭한 버튼의 부모 행 찾기
        var rowData = {
            id: $row.find('td').eq(1).text(), // 두 번째 <td>에서 ID 값 가져오기
            culturalPropertiesName: $row.find('td').eq(2).text(), // 세 번째 <td>에서 문화재 이름 가져오기
            categoryName: $row.find('td').eq(3).text(), // 네 번째 <td>에서 종목 가져오기
            region: $row.find('td').eq(4).text(), // 다섯 번째 <td>에서 지역 가져오기
            dynasty: $row.find('td').eq(5).text(), // 여섯 번째 <td>에서 시대 가져오기
            address: $row.find('td').eq(6).text(), // 일곱 번째 <td>에서 주소 가져오기
            registrationDate: $row.find('td').eq(7).text(), // 일곱 번째 <td>에서 지정일 가져오기
            classifyA: $row.find('td').eq(8).text(), // 여덟 번째 <td>에서 분류1 가져오기
            classifyB: $row.find('td').eq(9).text(), // 아홉 번째 <td>에서 분류2 가져오기
            classifyC: $row.find('td').eq(10).text(), // 열 번째 <td>에서 분류3 가져오기
            classifyD: $row.find('td').eq(11).text() // 열한 번째 <td>에서 분류4 가져오기

        };

        // 수정 입력 필드 추가
        var editRowHtml = '<tr class="edit-row">' +
            '<td colspan="17">' + // 전체 열을 차지하도록 colspan 설정
            '<input type="text" class="form-control edit-culturalPropertiesName" value="' + rowData.culturalPropertiesName + '" placeholder="문화재 이름">' +
            '<input type="text" class="form-control edit-categoryName" value="' + rowData.categoryName + '" placeholder="종목">' +
            '<input type="text" class="form-control edit-region" value="' + rowData.region + '" placeholder="지역">' +
            '<input type="text" class="form-control edit-dynasty" value="' + rowData.dynasty + '" placeholder="시대">' +
            '<input type="text" class="form-control edit-address" value="' + rowData.address + '" placeholder="주소">' +
            '<input type="text" class="form-control edit-registrationDate" value="' + rowData.registrationDate + '" placeholder="지정일">' +
            '<input type="text" class="form-control edit-classifyA" value="' + rowData.classifyA + '" placeholder="분류1">' +
            '<input type="text" class="form-control edit-classifyB" value="' + rowData.classifyB + '" placeholder="분류2">' +
            '<input type="text" class="form-control edit-classifyC" value="' + rowData.classifyC + '" placeholder="분류3">' +
            '<input type="text" class="form-control edit-classifyD" value="' + rowData.classifyD + '" placeholder="분류4">' +
            '<button class="btn btn-primary" id="saveDB">저장</button>' +
            '<button class="btn btn-secondary" id="cancelDB">취소</button>' +
            '</td></tr>';

        $row.after(editRowHtml); // 수정 입력 필드를 기존 행 아래에 추가
    }

    // 저장 버튼 클릭 이벤트
    $('#list1').on('click', '#saveDB', function () {
        var $editRow = $(this).closest('.edit-row'); // 수정 행을 찾음
        var id = $editRow.prev().find('td').eq(1).text(); // 수정할 데이터의 ID

        // 수정된 값 가져오기
        var culturalPropertiesName = $editRow.find('.edit-culturalPropertiesName').val();
        var categoryName = $editRow.find('.edit-categoryName').val();
        var region = $editRow.find('.edit-region').val();
        var dynasty = $editRow.find('.edit-dynasty').val();
        var address = $editRow.find('.edit-address').val();
        var registrationDate = $editRow.find('.edit-registrationDate').val();
        var classifyA = $editRow.find('.edit-classifyA').val();
        var classifyB = $editRow.find('.edit-classifyB').val();
        var classifyC = $editRow.find('.edit-classifyC').val();
        var classifyD = $editRow.find('.edit-classifyD').val();

        // 수정 확인 알림창
        var isConfirmed = confirm('수정 내용을 저장하시겠습니까?');
        if (!isConfirmed) {
            return; // 사용자가 취소를 선택한 경우 AJAX 요청을 하지 않음
        }


        // AJAX 요청을 보내 DB를 업데이트하는 코드 추가
        $.ajax({
            url: '/admin/cultural-properties-regulate/updateDBData', // 업데이트 URL
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify([{
                id: id,
                culturalPropertiesName: culturalPropertiesName,
                categoryName: categoryName,
                region: region,
                dynasty: dynasty,
                address: address,
                registrationDate: registrationDate,
                classifyA: classifyA,
                classifyB: classifyB,
                classifyC: classifyC,
                classifyD: classifyD
            }]),

            success: function (response) {
                console.log('Success response:', response); // 성공 응답 로그
                alert('수정이 완료되었습니다!');

                // 수정된 값을 테이블에 반영
                var $row = $editRow.prev(); // 원래 행
                $row.find('.culturalPropertiesName').text(culturalPropertiesName);
                $row.find('.categoryName').text(categoryName);
                $row.find('.region').text(region);
                $row.find('.dynasty').text(dynasty);
                $row.find('.address').text(address);
                $row.find('.registrationDate').text(registrationDate);
                $row.find('.classifyA').text(classifyA);
                $row.find('.classifyB').text(classifyB);
                $row.find('.classifyC').text(classifyC);
                $row.find('.classifyD').text(classifyD);

                // 수정 행 삭제
                $editRow.remove();
            },
            error: function (xhr, status, error) {
                console.error('수정 실패:', error);
                console.log(xhr.responseText); // 서버의 응답 내용 확인
                alert('수정에 실패했습니다. 다시 시도해주세요.');
            }
        });
    });


    // 취소 버튼 클릭 이벤트
    $('#list1').on('click', '#cancelDB', function () {
        $(this).closest('.edit-row').remove(); // 수정 행 삭제
    });



    // 초기 페이지 데이터 불러오기
    fetchApiData(currentPage2);
    getDBData(1); // 초기 DB 데이터 불러오기
    findListPage(1, 10);



    // 페이지 검색 기능 추가
    $('#searchPage2').on('keypress', function(event) {
        if (event.key === 'Enter') {
            const pageIndex = parseInt($(this).val());
            if (!isNaN(pageIndex) && pageIndex > 0) {
                fetchApiData(pageIndex);
            } else {
                alert('유효한 페이지 번호를 입력하세요.');
            }
        }
    });



    // 검색 버튼 클릭 시 페이지 이동
    $('#searchPageBtn').on('click', function() {
        var pageIndex = parseInt($('#searchPage2').val()) || 1; // 입력된 페이지 번호 가져오기 (기본값 1)
        if (!isNaN(pageIndex) && pageIndex > 0) {
            currentPage2 = pageIndex; // 현재 페이지 업데이트
            fetchApiData(currentPage2); // API 호출
        } else {
            alert('유효한 페이지 번호를 입력하세요.');
        }
    });


    $('#reset2').on('click', function() {
        // 페이지를 1로 초기화
        currentPage2 = 1;

        // 텍스트 박스 초기화
        $('#searchName2').val('');
        $('#searchRegion2').val('');
        $('#searchDynasty2').val('');
        $('#searchPage2').val('');

        // 초기 페이지 데이터 요청
        fetchApiData(currentPage2); // API 호출
    });


//----------------------관리자 api 검색 보류


//
//    $('#searchDB2').on('click', function() {
//            var pageIndex = 1; // 검색 시 첫 페이지로 초기화
//            searchAPIDataFilter(pageIndex);
//        });
//
//
//    function searchAPIDataFilter(pageIndex) {
//        $('#list2').html(""); // 테이블 내용 초기화
//        $('#loadingIndicator').show(); // 로딩 표시
//
//        // 검색 필드 값 가져오기
//        var categoryName = $('#categoryFilter2').val(); // 종목
//        var culturalPropertiesName = $('#searchName2').val(); // 문화재 이름
//        var region = $('#searchRegion2').val(); // 지역
//        var dynasty = $('#searchDynasty2').val(); // 시대
//
//
//        $.ajax({
//    //        url: '/admin/cultural-properties-regulate/searchAPI?pageIndex=' + pageIndex,
//    //        method: 'POST',
//    //        contentType: 'application/json',
//    //        data: JSON.stringify({
//    //            categoryName: categoryName,
//    //            culturalPropertiesName: culturalPropertiesName,
//    //            region: region,
//    //            dynasty: dynasty
//    //        }),
//    // url: '/admin/cultural-properties-regulate/searchAPI?pageIndex=' + pageIndex +
//    //             '&categoryName=' + categoryName +
//    //             '&culturalPropertiesName=' + culturalPropertiesName +
//    //             '&region=' + region +
//    //             '&dynasty=' + dynasty,
//
//    url: '/admin/cultural-properties-regulate/searchAPI?pageIndex=' + pageIndex +
//             '&categoryName=' + encodeURIComponent(categoryName) +
//             '&culturalPropertiesName=' + encodeURIComponent(culturalPropertiesName) +
//             '&region=' + encodeURIComponent(region) +
//             '&dynasty=' + encodeURIComponent(dynasty),
//            method: 'POST',
//            contentType: 'application/json',
//            beforeSend: function() {
//                $('#loadingIndicator').show(); // Ajax 요청 전에 로딩 표시 보이기
//            },
//            success: function(list) {
//                $.each(list, function(index, culturalProperties) {
//                    var index1 = (pageIndex - 1) * itemsPerPage2 + index + 1;
//
//                    var htmlCheck = '<tr><td><input class="check2" type="checkbox" name="index" value="' + index + '"/></td>';
//                    var htmlContent =
//                        '<td class="culturalPropertieId">' + index1 + '</td>' +
//                        '<td class="culturalPropertiesName">' + culturalProperties.culturalPropertiesName + '</td>' +
//                        '<td class="categoryName">' + culturalProperties.categoryName + '</td>' +
//                        '<td class="region">' + culturalProperties.region + '</td>' +
//                        '<td class="dynasty">' + culturalProperties.dynasty + '</td></tr>';
//
//                    var finalHtml = htmlCheck + htmlContent;
//                    $('#list2').append(finalHtml);
//                });
//
//                // 페이지네이션 다시 그리기
//                renderSearchAPIPagination(pageIndex, list.length); // 페이지네이션 그리기
//
//                // 데이터를 모두 표시한 후 로딩 인디케이터 숨기기
//                setTimeout(function() {
//                    $('#loadingIndicator').hide();
//                });
//            },
//            error: function(xhr, status, error) {
//                console.error('API 호출 오류:', error);
//            }
//        });
//    }
//
//    // 페이지 버튼 생성 함수
//    function renderSearchAPIPagination(pageIndex, totalItems) {
//        $('#paginationSection2').empty(); // 기존 버튼 제거
//
//        // 페이지 수 계산 (가정: 10개씩 표시)
//        var itemsPerPage = 10;
//        var totalPages = Math.ceil(totalItems / itemsPerPage);
//
//        // 이전 버튼
//        var prevHtml = '<li class="page-item ' + (pageIndex == 1 ? 'disabled' : '') + '">' +
//            '<a class="page-link prev-link" href="#" data-page="' + (pageIndex - 1) + '">이전</a>' +
//            '</li>';
//        $('#paginationSection2').append(prevHtml);
//
//        // 페이지 번호 버튼
//        var startPage = (Math.ceil(pageIndex / 10) - 1) * 10 + 1;
//        var endPage = Math.min(startPage + 9, totalPages);
//
//        for (var pageNum = startPage; pageNum <= endPage; pageNum++) {
//            var pageHtml = '<li class="page-item ' + (pageNum == pageIndex ? 'active' : '') + '">' +
//                '<a class="page-link page-num" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
//                '</li>';
//            $('#paginationSection2').append(pageHtml);
//        }
//
//        // 다음 버튼
//        var nextHtml = '<li class="page-item ' + (pageIndex >= totalPages ? 'disabled' : '') + '">' +
//            '<a class="page-link next-link" href="#" data-page="' + (pageIndex + 1) + '">다음</a>' +
//            '</li>';
//        $('#paginationSection2').append(nextHtml);
//
//        // 페이지 번호 버튼 클릭 이벤트
//        $('.page-num').on('click', function() {
//            var selectedPage = $(this).data('page');
//            searchAPIDataFilter(selectedPage);
//        });
//
//        // 이전 버튼 클릭 이벤트
//        $('.prev-link').on('click', function(e) {
//            e.preventDefault();
//            if (pageIndex > 1) {
//                searchAPIDataFilter(pageIndex - 1);
//            }
//        });
//
//        // 다음 버튼 클릭 이벤트
//        $('.next-link').on('click', function(e) {
//            e.preventDefault();
//            if (pageIndex < totalPages) {
//                searchAPIDataFilter(pageIndex + 1);
//            }
//        });
//    }

//---------------------------------관리자 api 검색 보류 //22//

//
//$('#searchDB2').on('click', function() {
//        var pageIndex = 1; // 검색 시 첫 페이지로 초기화
//        searchAPIDataFilter(pageIndex);
//    });
//
//
//function searchAPIDataFilter(pageIndex) {
//    $('#list2').html(""); // 테이블 내용 초기화
////    $('#loadingIndicator').show(); // 로딩 표시
//
//    // 검색 필드 값 가져오기
//    var categoryName = $('#categoryFilter2').val(); // 종목
//    var culturalPropertiesName = $('#searchName2').val(); // 문화재 이름
//    var region = $('#searchRegion2').val(); // 지역
//    var dynasty = $('#searchDynasty2').val(); // 시대
//
//    console.log("pageIndex : " + pageIndex)
//    console.log("categoryName : " + categoryName)
//    console.log("culturalPropertiesName : " + culturalPropertiesName)
//    console.log("region : " + region)
//    console.log("dynasty : " + dynasty)
//
//
//    $.ajax({
//        url: '/admin/cultural-properties-regulate/searchAPI?pageIndex='+pageIndex+'&categoryName='+categoryName+'&culturalPropertiesName='+culturalPropertiesName+'&region='+region+'&dynasty='+dynasty,
//        method: 'POST',
//        contentType: 'application/json',
////        data: {
////            pageIndex : pageIndex,
////            categoryName: categoryName,
////            culturalPropertiesName: culturalPropertiesName,
////            region: region,
////            dynasty: dynasty
////        },
////        beforeSend: function() {
////            $('#loadingIndicator').show(); // Ajax 요청 전에 로딩 표시 보이기
////        },
//        success: function(list) {
//
//            console.log("성공??");
//            $.each(list, function(index, culturalProperties) {
//                var index1 = (pageIndex - 1) * itemsPerPage2 + index + 1;
//
//                var htmlCheck = '<tr><td><input class="check2" type="checkbox" name="index" value="' + index + '"/></td>';
//                var htmlContent =
//                    '<td class="culturalPropertieId">' + index1 + '</td>' +
//                    '<td class="culturalPropertiesName">' + culturalProperties.culturalPropertiesName + '</td>' +
//                    '<td class="categoryName">' + culturalProperties.categoryName + '</td>' +
//                    '<td class="region">' + culturalProperties.region + '</td>' +
//                    '<td class="dynasty">' + culturalProperties.dynasty + '</td></tr>';
//
//                var finalHtml = htmlCheck + htmlContent;
//                $('#list2').append(finalHtml);
//            });
//
//            // 페이지네이션 다시 그리기
//            renderSearchAPIPagination(pageIndex, list.length); // 페이지네이션 그리기
//
//            // 데이터를 모두 표시한 후 로딩 인디케이터 숨기기
//            setTimeout(function() {
//                $('#loadingIndicator').hide();
//            });
//        },
//        error: function(xhr, status, error) {
//            console.error('API 호출 오류:' + error);
//        }
//    });
//}
//
//// 페이지 버튼 생성 함수
//function renderSearchAPIPagination(pageIndex, totalItems) {
//    $('#paginationSection2').empty(); // 기존 버튼 제거
//
//    // 페이지 수 계산 (가정: 10개씩 표시)
//    var itemsPerPage = 10;
//    var totalPages = Math.ceil(totalItems / itemsPerPage);
//
//    // 이전 버튼
//    var prevHtml = '<li class="page-item ' + (pageIndex == 1 ? 'disabled' : '') + '">' +
//        '<a class="page-link prev-link" href="#" data-page="' + (pageIndex - 1) + '">이전</a>' +
//        '</li>';
//    $('#paginationSection2').append(prevHtml);
//
//    // 페이지 번호 버튼
//    var startPage = (Math.ceil(pageIndex / 10) - 1) * 10 + 1;
//    var endPage = Math.min(startPage + 9, totalPages);
//
//    for (var pageNum = startPage; pageNum <= endPage; pageNum++) {
//        var pageHtml = '<li class="page-item ' + (pageNum == pageIndex ? 'active' : '') + '">' +
//            '<a class="page-link page-num" href="#" data-page="' + pageNum + '">' + pageNum + '</a>' +
//            '</li>';
//        $('#paginationSection2').append(pageHtml);
//    }
//
//    // 다음 버튼
//    var nextHtml = '<li class="page-item ' + (pageIndex >= totalPages ? 'disabled' : '') + '">' +
//        '<a class="page-link next-link" href="#" data-page="' + (pageIndex + 1) + '">다음</a>' +
//        '</li>';
//    $('#paginationSection2').append(nextHtml);
//
//    // 페이지 번호 버튼 클릭 이벤트
//    $('.page-num').on('click', function() {
//        var selectedPage = $(this).data('page');
//        searchAPIDataFilter(selectedPage);
//    });
//
//    // 이전 버튼 클릭 이벤트
//    $('.prev-link').on('click', function(e) {
//        e.preventDefault();
//        if (pageIndex > 1) {
//            searchAPIDataFilter(pageIndex - 1);
//        }
//    });
//
//    // 다음 버튼 클릭 이벤트
//    $('.next-link').on('click', function(e) {
//        e.preventDefault();
//        if (pageIndex < totalPages) {
//            searchAPIDataFilter(pageIndex + 1);
//        }
//    });
//}
//------------------------------------------

    $('#searchDB').on('click', function() {
        $('#list1').empty();

        var category = $('#categoryFilter').val();
        var name = $('#searchName').val();
        var region = $('#searchRegion').val();
        var dynasty = $('#searchDynasty').val();

        // 검색 결과의 총 개수 조회
        $.ajax({
            url: '/admin/cultural-properties-regulate/searchCount',
            method: 'GET',
            data: {
                category: category,
                name: name,
                region: region,
                dynasty: dynasty
            },
            success: function(totalCount) {
                console.log('총 검색 결과 개수:', totalCount);
                // 첫 번째 페이지 로드
                searchDBCulturalProperties(category, name, region, dynasty, 1, totalCount);
            },
            error: function(xhr, status, error) {
                console.error('검색 결과 개수 조회 오류:', error);
            }
        });
    });


    //  초기화 버튼 클릭 이벤트
    $('#reset').on('click', function() {
        // 검색 필터 초기화
        $('#categoryFilter').val('all');
        $('#searchName').val('');
        $('#searchRegion').val('');
        $('#searchDynasty').val('');

        // 기본 데이터 로드
        findListPage(currentPage, 10);

        renderPagination1(currentPage, totalPages);
        getDBData(1);


    });

    function searchDBCulturalProperties(category, name, region, dynasty, page, totalCount) {
        $.ajax({
            url: '/admin/cultural-properties-regulate/searchDB',
            method: 'POST',
            data: {
                category: category,
                name: name,
                region: region,
                dynasty: dynasty,
                page: page
            },
            success: function(response) {
                console.log('검색 결과:', response);

                disableCheckboxes();

                if (response && response.length > 0) {
                    searchDBTable(response); // 검색 결과 테이블 업데이트

                    var totalPages = Math.ceil(totalCount / 10); // 한 페이지에 10개씩 표시

                    renderSearchPagination(page, totalPages, category, name, region, dynasty, totalCount); // 페이지네이션 업데이트

                } else {
                    $('#list1').empty();
                    $('#paginationSection1').empty();
                    alert('일치하는 데이터가 존재하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.error('검색 오류:', error);
            }
        });
    }

    function renderSearchPagination(currentPage, totalPages, category, name, region, dynasty, totalCount) {
        const paginationContainer = $('#paginationSection1');
        paginationContainer.empty();


        // 이전 버튼
        if (currentPage > 1) {
            paginationContainer.append(`
                <li class="page-item" id="prev-btn">
                    <a class="page-link" href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            `);
        }

        // 페이지 버튼 생성
        for (let i = 1; i <= totalPages; i++) {
            const pageButton = $('<li>')
                .addClass('page-item')
                .append($('<a>').addClass('page-link').text(i).attr('href', '#').data('page', i));

            if (i === currentPage) {
                pageButton.addClass('active');
            }

            paginationContainer.append(pageButton);
        }

        // 다음 버튼
        if (currentPage < totalPages) {
            paginationContainer.append(`
                <li class="page-item" id="next-btn">
                    <a class="page-link" href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            `);
        }

        // 페이지 버튼 클릭 이벤트 등록
        paginationContainer.find('.page-link').on('click', function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            if (page) {
                searchDBCulturalProperties(category, name, region, dynasty, page, totalCount);
            }
        });

        // 이전 버튼 클릭 이벤트 등록
        $('#prev-btn').on('click', function(e) {
            e.preventDefault();
            if (currentPage > 1) {
                const prevPage = currentPage - 1;
                searchDBCulturalProperties(category, name, region, dynasty, prevPage, totalCount);
            }
        });

        // 다음 버튼 클릭 이벤트 등록
        $('#next-btn').on('click', function(e) {
            e.preventDefault();
            if (currentPage < totalPages) {
                const nextPage = currentPage + 1;
                searchDBCulturalProperties(category, name, region, dynasty, nextPage, totalCount);
            }
        });
    }

    // 초기화 버튼 클릭 이벤트
    $('#reset').on('click', function() {
        // 검색 필터 초기화
        $('#categoryFilter').val('all');
        $('#searchName').val('');
        $('#searchRegion').val('');
        $('#searchDynasty').val('');

        // 첫 페이지 로드
        searchDBCulturalProperties('all', '', '', '', 1, 0); // totalCount가 0이어도 처리할 수 있어야 함
    });






    function searchDBTable(list) {
        $('#list1').empty(); // 기존 데이터 초기화

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

    }




});

