document.addEventListener('DOMContentLoaded', function() {
    const imageUpload = document.getElementById('imageUpload');
    const editor = document.getElementById('editor');

    imageUpload.addEventListener('change', async function(e) {
        const file = e.target.files[0];
        if (file) {
            try {
                const formData = new FormData();
                const filePath = `/board/${new Date().getTime()}.png`
                formData.append('file', file);
                formData.append('path', filePath);


                // 서버로 이미지 전송
                const response =  await axios.post('/file/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                // 에디터에 이미지 삽입
                const img = document.createElement('img');
                img.src = "https://kr.object.ncloudstorage.com/team3" + filePath;
                img.style.maxWidth = '100%';

                editor.appendChild(img);
            } catch (error) {
                console.error('Error uploading image:', error);
                alert('이미지 업로드에 실패했습니다.');
            }
        }
    });

    document.getElementById('writeForm').addEventListener('submit',  async function(e) {
        e.preventDefault();
        const title = document.getElementById('title').value;
        const content = editor.innerHTML;
        const category = document.getElementById('category').value;

        console.log('제목:', title);
        console.log('내용:', content);
        console.log('category:', category);

        await axios.post('/board',{
            title,
            content,
            genre: category
        })

        alert('글이 작성되었습니다.');
        history.back()
    });
});