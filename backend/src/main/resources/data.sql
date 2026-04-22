/*-- Member 데이터 (관리자)
INSERT INTO member (email, password, nickname, role, provider, created_at, updated_at)
VALUES ('admin@test.com', '1234', '관리자', 'ADMIN', 'LOCAL', NOW(), NOW());

-- Member 데이터 (일반 유저)
INSERT INTO member (email, password, nickname, role, provider, created_at, updated_at)
VALUES ('user@test.com', '1234', '유저', 'USER', 'KAKAO', NOW(), NOW());

-- Cafe 데이터
INSERT INTO cafe (member_id, name, address, latitude, longitude, phone, description, type, franchise, has_toilet, has_outlet, has_wifi, floor_count, has_separate_space, congestion_level, image_url, status, created_at, updated_at)
VALUES (1, '스타벅스 강남점', '서울시 강남구 강남대로 390', 37.4979, 127.0276, '02-1234-5678', '조용하고 넓은 카페', 'FRANCHISE', 'STARBUCKS', true, true, true, 'TWO', false, 'LOW', null, 'APPROVED', NOW(), NOW());

INSERT INTO cafe (member_id, name, address, latitude, longitude, phone, description, type, franchise, has_toilet, has_outlet, has_wifi, floor_count, has_separate_space, congestion_level, image_url, status, created_at, updated_at)
VALUES (1, '메가커피 역삼점', '서울시 강남구 역삼동 123', 37.4965, 127.0283, '02-2345-6789', '가성비 좋은 카페', 'FRANCHISE', 'MEGA_COFFEE', true, true, true, 'ONE', false, 'MEDIUM', null, 'APPROVED', NOW(), NOW());

INSERT INTO cafe (member_id, name, address, latitude, longitude, phone, description, type, franchise, has_toilet, has_outlet, has_wifi, floor_count, has_separate_space, congestion_level, image_url, status, created_at, updated_at)
VALUES (1, '카페 온도', '서울시 강남구 논현동 456', 37.5100, 127.0400, '02-3456-7890', '분위기 좋은 개인 카페', 'INDIVIDUAL', 'NONE', true, true, true, 'ONE', true, 'LOW', null, 'APPROVED', NOW(), NOW());

INSERT INTO cafe (member_id, name, address, latitude, longitude, phone, description, type, franchise, has_toilet, has_outlet, has_wifi, floor_count, has_separate_space, congestion_level, image_url, status, created_at, updated_at)
VALUES (1, '카페 봄', '서울시 강남구 삼성동 789', 37.5080, 127.0610, '02-4567-8901', '넓고 쾌적한 카페', 'INDIVIDUAL', 'NONE', true, false, true, 'THREE_OR_MORE', true, 'HIGH', null, 'APPROVED', NOW(), NOW());

INSERT INTO cafe (member_id, name, address, latitude, longitude, phone, description, type, franchise, has_toilet, has_outlet, has_wifi, floor_count, has_separate_space, congestion_level, image_url, status, created_at, updated_at)
VALUES (1, '승인대기 카페', '서울시 강남구 청담동 111', 37.5200, 127.0500, null, '승인 대기 중인 카페', 'INDIVIDUAL', 'NONE', true, true, false, 'ONE', false, 'LOW', null, 'PENDING', NOW(), NOW());

-- Wishlist 데이터
INSERT INTO wishlist (member_id, cafe_id, created_at, updated_at)
VALUES (2, 1, NOW(), NOW());

INSERT INTO wishlist (member_id, cafe_id, created_at, updated_at)
VALUES (2, 2, NOW(), NOW());

INSERT INTO wishlist (member_id, cafe_id, created_at, updated_at)
VALUES (2, 3, NOW(), NOW());*/