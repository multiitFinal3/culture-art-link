let currentCategory = 'all';
let currentPage = 1;
const postsPerPage = 10;

async function fetchPosts(category = 'all', page = 1, query = '') {
    let url = `/board?genre=${category}`;
    if (query) {
        url += `&query=${encodeURIComponent(query)}`;
    }

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();

        console.log('data : ', data)
        return { posts: data, totalPosts: data.length } ;
    } catch (error) {
        console.error('Error fetching posts:', error);
        return { posts: [], totalPosts: 0 };
    }
}

async function displayPosts(category = 'all', page = 1, query = '') {
    const { posts, totalPosts } = await fetchPosts(category, page, query);
    const postList = document.getElementById('postList');
    postList.innerHTML = '';

    posts.forEach(post => {
        const row = `
            <tr>
                <td>${post.id}</td>
                <td><a href="/board/view/${post.id}">${post.title}</a></td>
                <td>${post.author}</td>
                <td>${post.createdAt}</td>
            </tr>
        `;
        postList.innerHTML += row;
    });

    displayPagination(totalPosts, page);
}

function displayPagination(totalPosts, currentPage) {
    const pageCount = Math.ceil(totalPosts / postsPerPage);
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    for (let i = 1; i <= pageCount; i++) {
        const button = document.createElement('button');
        button.innerText = i;
        button.addEventListener('click', () => {
            displayPosts(currentCategory, i);
        });
        if (i === currentPage) {
            button.classList.add('active');
        }
        pagination.appendChild(button);
    }
}

function searchPosts() {
    const searchTerm = document.getElementById('searchInput').value;
    currentPage = 1;
    displayPosts(currentCategory, currentPage, searchTerm);
}

// 탭 이벤트 리스너 설정
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelector('.tab.active').classList.remove('active');
        tab.classList.add('active');
        currentCategory = tab.dataset.category;
        currentPage = 1;
        displayPosts(currentCategory, currentPage);
    });
});

// 초기 게시글 표시
displayPosts(currentCategory, currentPage);