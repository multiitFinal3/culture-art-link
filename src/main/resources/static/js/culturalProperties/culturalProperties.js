$(document).ready(function() {

    let currentPage = 1; // 현재 페이지
    const pageSize = 6; // 페이지당 데이터 수

    // 페이지가 로드되면 문화재 목록을 불러옴
    loadDBList(currentPage, pageSize); // 첫 페이지, 한 번에 6개 항목 불러오기

    function loadDBList(page, size) {
        $.ajax({
            url: `/cultural-properties/getList?page=${page}&size=${size}`,
            type: 'GET',
            success: function(data) {
                console.log('데이터 수신 성공:', data);
                renderCulturalProperties(data.culturalProperties); // 문화재 목록 렌더링
                updatePagination(data.totalPages, page); // 페이지 버튼 업데이트
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패:', status, error);
            }
        });
    }

    function renderCulturalProperties(properties) {
        const gallerySection = $('#gallery-section .row');
        gallerySection.empty(); // 기존 콘텐츠 초기화

        if (properties.length === 0) {
            gallerySection.append('<p>등록된 문화재가 없습니다.</p>'); // 데이터가 없을 경우 메시지 추가
            return;
        }

        // 각 문화재에 대한 카드 생성
        $.each(properties, function(index, property) {
            const colDiv = $('<div>').addClass('col');
            const cardDiv = $('<div>').addClass('card h-100').css('cursor', 'pointer').click(function() {
                window.location.href = `/cultural-properties/detail/${property.id}`; // ID에 따라 URL 수정
            });

            const img = $('<img>').addClass('card-img-top')
                .attr('src', property.mainImgUrl)
                .attr('alt', property.culturalPropertiesName);

            const cardBodyDiv = $('<div>').addClass('card-body');
            const title = $('<h5>').addClass('card-title').text(property.culturalPropertiesName);
            const locationText = $('<p>').addClass('card-text')
                .text((property.region && property.district) ? `${property.region} ${property.district}` : '위치정보가 없습니다');


            // 배지 추가
            const badgeDiv = $('<div>').addClass('d-flex justify-content-end position-absolute bottom-0 end-0 p-2');
            const likeBadge = $('<span>')
                .addClass('badge bg-primary me-1')
                .text('♥')
                .css('cursor', 'pointer')
                .data('clicked', false) // 클릭 상태 초기화
                .click(function(e) {
                    e.stopPropagation(); // 클릭 이벤트 전파 방지
                    const isClicked = $(this).data('clicked'); // 클릭 상태 가져오기
                    if (isClicked) {
                        cardDiv.css('background-color', 'white'); // 다시 클릭 시 흰색으로 변경
                    } else {
                        cardDiv.css('background-color', '#ffe5ea'); // 첫 클릭 시 핑크색으로 변경
                    }
                    $(this).data('clicked', !isClicked); // 클릭 상태 토글
                });

            const starBadge = $('<span>')
                .addClass('badge bg-secondary')
                .text('☆')
                .css('cursor', 'pointer')
                .data('clicked', false) // 클릭 상태 초기화
                .click(function(e) {
                    e.stopPropagation(); // 클릭 이벤트 전파 방지
                    const isClicked = $(this).data('clicked'); // 클릭 상태 가져오기
                    if (isClicked) {
                        cardDiv.css('background-color', 'white'); // 다시 클릭 시 흰색으로 변경
                    } else {
                        cardDiv.css('background-color', '#dfdfdf'); // 첫 클릭 시 회색으로 변경
                    }
                    $(this).data('clicked', !isClicked); // 클릭 상태 토글
                });


            // 배지 구성
            badgeDiv.append(likeBadge).append(starBadge);


            // 카드 구성
            cardBodyDiv.append(title).append(locationText);
            cardBodyDiv.append(badgeDiv); // 배지를 카드 본문에 추가
            cardDiv.append(img).append(cardBodyDiv);
            colDiv.append(cardDiv);
            gallerySection.append(colDiv);
        });
    }


    function updatePagination(totalPages, currentPage) {
        const paginationContainer = $('#pagination');
        paginationContainer.empty(); // 기존 페이지 버튼 비우기

        // 버튼 범위 계산
        const maxButtons = 10; // 최대 버튼 수
        const halfButtons = Math.floor(maxButtons / 2); // 각 방향으로 보여줄 버튼 수
        let startPage, endPage;

        if (totalPages <= maxButtons) {
            // 총 페이지 수가 최대 버튼 수보다 작거나 같으면 모든 페이지 버튼 표시
            startPage = 1;
            endPage = totalPages;
        } else {
            // 총 페이지 수가 최대 버튼 수보다 많으면 버튼 범위를 설정
            if (currentPage <= halfButtons) {
                startPage = 1;
                endPage = maxButtons;
            } else if (currentPage + halfButtons >= totalPages) {
                startPage = totalPages - maxButtons + 1;
                endPage = totalPages;
            } else {
                startPage = currentPage - halfButtons;
                endPage = currentPage + halfButtons;
            }
        }

        // 페이지 버튼 생성
        for (let i = startPage; i <= endPage; i++) {
            const pageButton = $('<li>')
                .addClass('page-item')
                .append($('<a>').addClass('page-link').text(i).attr('href', '#').data('page', i));

            if (i === currentPage) {
                pageButton.addClass('active'); // 현재 페이지에 active 클래스 추가
            }

            paginationContainer.append(pageButton);
        }

        // 이전 버튼
        if (currentPage > 1) {
            paginationContainer.prepend(`
                <li class="page-item" id="prev-btn">
                    <a class="page-link" href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            `);
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

        // 페이지 버튼 클릭 이벤트
        $('.page-link').on('click', function(e) {
            e.preventDefault(); // 기본 링크 동작 방지
            const page = $(this).data('page');
            if (page) {
                currentPage = page; // 클릭한 페이지로 설정
                loadDBList(currentPage, pageSize); // 데이터를 다시 로드
            }
        });

        // 이전 버튼 클릭 이벤트
        $('#prev-btn').on('click', function(e) {
            e.preventDefault(); // 기본 링크 동작 방지
            if (currentPage > 1) {
                currentPage--; // 이전 페이지로 설정
                loadDBList(currentPage, pageSize); // 데이터 로드
            }
        });

        // 다음 버튼 클릭 이벤트
        $('#next-btn').on('click', function(e) {
            e.preventDefault(); // 기본 링크 동작 방지
            if (currentPage < totalPages) {
                currentPage++; // 다음 페이지로 설정
                loadDBList(currentPage, pageSize); // 데이터 로드
            }
        });
    }

});

