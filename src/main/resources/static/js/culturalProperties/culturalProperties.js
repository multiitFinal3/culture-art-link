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


    loadNewsArticles(); // 페이지가 로드될 때 뉴스 기사를 불러옴

    function loadNewsArticles() {
        $.ajax({
            url: '/cultural-properties/news', // 뉴스 기사 요청 URL
            method: 'GET',
            success: function(data) {
                console.log('뉴스 기사를 성공적으로 수신:', data);
                renderNewsArticles(data); // 뉴스 기사 렌더링
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패:', status, error);
            }
        });
    }


    function renderNewsArticles(articles) {
        const newsList = $('#news-list');
        newsList.empty(); // 기존 내용을 비웁니다.

        if (!Array.isArray(articles) || articles.length === 0) {
            newsList.append('<li class="list-group-item">등록된 문화재 관련 뉴스가 없습니다.</li>');
            return;
        }

        $.each(articles, function(index, article) {
            const listItem = $('<li>').addClass('list-group-item');

            // 이미지 추가
            if (article.imgUrl) {
                const image = $('<img>').attr('src', article.imgUrl).addClass('img-fluid mb-2'); // 이미지에 클래스 추가
                listItem.append(image); // 이미지 리스트 항목에 추가
            }
//            console.log('뉴스 기사 이미지 URL:', article.imgUrl);

            const title = $('<h5>').addClass('mb-1').text(article.title);
            const content = $('<p>').addClass('mb-1').text(article.content);
            const date = $('<small>').addClass('text-muted').text(article.date);
            const link = $('<a>').attr('href', article.link).addClass('btn btn-primary btn-sm mt-2').text('자세히 보기');

            listItem.append(title, content, date, link);
            newsList.append(listItem);
        });
    }


    loadVideos();

    function loadVideos() {
        const query = "국가유산"; // 이 부분에 원하는 검색어를 넣으세요.
        $.ajax({
            url: '/cultural-properties/videos?query=' + encodeURIComponent(query), // 서버에서 비디오 데이터를 가져오는 URL
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded', // POST 요청 시 Content-Type 설정
            success: function(videos) {
                console.log('받은 비디오 데이터:', videos); // 응답 데이터 로그
                renderVideos(videos); // 성공적으로 데이터를 가져온 경우
            },
            error: function(xhr, status, error) {
                console.error('비디오 로드 실패:', status, error);
            }
        });
    }


    function renderVideos(videos) {
        const videoList = $('#video-list');
        videoList.empty(); // 기존 내용을 비웁니다.

        if (!Array.isArray(videos) || videos.length === 0) {
            videoList.append('<p>등록된 영상이 없습니다.</p>');
            return;
        }

        // 비디오 리스트 렌더링
        $.each(videos, function(index, video) {
            const colDiv = $('<div>').addClass('col-md-4 mb-4');
            const cardDiv = $('<div>').addClass('card');
            const cardBody = $('<div>').addClass('card-body');

            // 제목
            const title = $('<h5>').addClass('card-title').html(video.title); // html() 메서드로 변경

            // iframe 추가 (비디오 재생)
            const videoId = video.link.split('v=')[1]; // 링크에서 videoId 추출
            const iframe = $('<iframe>')
                .attr('src', 'https://www.youtube.com/embed/' + videoId)
                .attr('class', 'embed-responsive-item')
                .attr('allowfullscreen', true)
                .css('width', '100%')
                .css('height', '250px');

            cardBody.append(title, iframe); // 제목과 iframe을 카드 바디에 추가
            cardDiv.append(cardBody);
            colDiv.append(cardDiv);
            videoList.append(colDiv); // 비디오 카드 추가
        });
    }

});

