let currentCategory = 'performance';
let currentPage = 1;
const postsPerPage = 10;
let allPosts = [];

async function fetchPosts(category = 'all', query = '') {
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
        console.log('data : ', data);
        return data;
    } catch (error) {
        console.error('Error fetching posts:', error);
        return [];
    }
}

async function displayPosts(category = 'all', page = 1, query = '') {
    currentCategory = category;
    currentPage = page;
    console.log('query || category : ',query , category, currentCategory)
    allPosts = await fetchPosts(category, query);

    const startIndex = (page - 1) * postsPerPage;
    const endIndex = startIndex + postsPerPage;
    const postsToDisplay = allPosts.slice(startIndex, endIndex);

    const postList = document.getElementById('postList');
    postList.innerHTML = '';
    postsToDisplay.forEach(post => {
        const row = `
            <tr>
                <td>${post.id}</td>
                <td><a href="/board/view/${post.id}">${post.title}</a></td>
                <td>${post.author}</td>
                <td>${post.createdAt}</td>
                <td>${post.commentSize}</td>
            </tr>
        `;
        postList.innerHTML += row;
    });

    displayPagination(allPosts.length, page);
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
    displayPosts(currentCategory, 1, searchTerm);
}

document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            tabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            const category = this.getAttribute('data-category');
            console.log('선택된 카테고리:', category);
            displayPosts(category, 1);
        });
    });

    // 초기 게시글 표시
    displayPosts(currentCategory, currentPage);
});