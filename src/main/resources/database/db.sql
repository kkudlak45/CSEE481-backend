--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.3

-- Started on 2022-06-28 10:22:45 EDT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 16400)
-- Name: Account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Account" (
    username character varying(50) NOT NULL,
    picture character varying(200),
    password character(60) NOT NULL,
    name character varying(50) NOT NULL,
    "joinDate" date DEFAULT CURRENT_DATE NOT NULL,
    interests character varying(200),
    id integer NOT NULL,
    email character varying(50) NOT NULL,
    "birthDate" date
);


ALTER TABLE public."Account" OWNER TO postgres;

--
-- TOC entry 3304 (class 0 OID 0)
-- Dependencies: 200
-- Name: TABLE "Account"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."Account" IS 'Table to be used for Account objects';


--
-- TOC entry 206 (class 1259 OID 16440)
-- Name: Account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Account" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Account_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 204 (class 1259 OID 16432)
-- Name: Card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Card" (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    picture character varying(200) NOT NULL,
    relationship character varying(50),
    "deckId" integer NOT NULL
);


ALTER TABLE public."Card" OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16450)
-- Name: Card_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Card" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Card_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 201 (class 1259 OID 16411)
-- Name: CurveData; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."CurveData" (
    id integer NOT NULL,
    "gameType" integer NOT NULL,
    "bestStat" numeric,
    "recentStat" numeric,
    "accountId" integer NOT NULL
);


ALTER TABLE public."CurveData" OWNER TO postgres;

--
-- TOC entry 3305 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE "CurveData"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."CurveData" IS 'A table to store "shortcut data" for drawing a player''s percentile on a standard normal curve';


--
-- TOC entry 207 (class 1259 OID 16443)
-- Name: CurveData_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."CurveData" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."CurveData_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 203 (class 1259 OID 16427)
-- Name: Deck; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Deck" (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    "creationDate" date DEFAULT CURRENT_DATE NOT NULL,
    "lastUsed" date DEFAULT CURRENT_DATE NOT NULL,
    "accountId" integer NOT NULL
);


ALTER TABLE public."Deck" OWNER TO postgres;

--
-- TOC entry 3306 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE "Deck"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."Deck" IS 'effectively a container for card objects';


--
-- TOC entry 205 (class 1259 OID 16437)
-- Name: Deck_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Deck" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Deck_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 202 (class 1259 OID 16419)
-- Name: PersonalData; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."PersonalData" (
    id integer NOT NULL,
    "gameType" integer NOT NULL,
    stat numeric,
    "accountId" integer NOT NULL
);


ALTER TABLE public."PersonalData" OWNER TO postgres;

--
-- TOC entry 3307 (class 0 OID 0)
-- Dependencies: 202
-- Name: TABLE "PersonalData"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."PersonalData" IS 'Stores all of a players'' performance data ever';


--
-- TOC entry 209 (class 1259 OID 16463)
-- Name: PersonalData_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."PersonalData" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."PersonalData_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3289 (class 0 OID 16400)
-- Dependencies: 200
-- Data for Name: Account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Account" (username, picture, password, name, "joinDate", interests, id, email, "birthDate") FROM stdin;
kudlkak	\N	asdf1234                                                    	kryzstof	2022-06-22	computer science	2	kkudlak45@gmail.com	2001-08-14
kudlkak	\N	asdf1234                                                    	kryzstof	2022-06-22	computer science	3	kkudlak45@gmail.com	2001-08-14
kudlkak	\N	asdf1234                                                    	kryzstof	2022-06-22	computer science	4	kkudlak45@gmail.com	2001-08-14
kudlkak	\N	asdf1234                                                    	abckryzstof123	2022-06-22	computer science	5	kkudlak45@gmail.com	2001-08-14
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	computers and stuff	6	example@example.com	2001-08-14
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	computers and stuff	7	example@example.com	2001-08-14
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	8	example@example.com	2001-08-14
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	9	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	10	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	11	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	12	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	14	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	15	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	17	example@example.com	\N
kkudlak45	\N	somebcrypthashedpassword                                    	Kryzstof Kudlak	2022-06-22	\N	19	example@example.com	\N
kkudlak45	\N	somepassword                                                	Kryzstof Kudlak	2022-06-28	\N	21	example@example.com	\N
kkudlak45	\N	somePassword                                                	Kryzstof Kudlak	2022-06-28	\N	22	example@example.com	\N
\.


--
-- TOC entry 3293 (class 0 OID 16432)
-- Dependencies: 204
-- Data for Name: Card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Card" (id, name, picture, relationship, "deckId") FROM stdin;
\.


--
-- TOC entry 3290 (class 0 OID 16411)
-- Dependencies: 201
-- Data for Name: CurveData; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."CurveData" (id, "gameType", "bestStat", "recentStat", "accountId") FROM stdin;
\.


--
-- TOC entry 3292 (class 0 OID 16427)
-- Dependencies: 203
-- Data for Name: Deck; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Deck" (id, name, "creationDate", "lastUsed", "accountId") FROM stdin;
\.


--
-- TOC entry 3291 (class 0 OID 16419)
-- Dependencies: 202
-- Data for Name: PersonalData; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."PersonalData" (id, "gameType", stat, "accountId") FROM stdin;
\.


--
-- TOC entry 3308 (class 0 OID 0)
-- Dependencies: 206
-- Name: Account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Account_id_seq"', 22, true);


--
-- TOC entry 3309 (class 0 OID 0)
-- Dependencies: 208
-- Name: Card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Card_id_seq"', 1, false);


--
-- TOC entry 3310 (class 0 OID 0)
-- Dependencies: 207
-- Name: CurveData_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."CurveData_id_seq"', 1, false);


--
-- TOC entry 3311 (class 0 OID 0)
-- Dependencies: 205
-- Name: Deck_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Deck_id_seq"', 1, true);


--
-- TOC entry 3312 (class 0 OID 0)
-- Dependencies: 209
-- Name: PersonalData_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."PersonalData_id_seq"', 1, false);


--
-- TOC entry 3146 (class 2606 OID 16407)
-- Name: Account Account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Account"
    ADD CONSTRAINT "Account_pkey" PRIMARY KEY (id);


--
-- TOC entry 3154 (class 2606 OID 16436)
-- Name: Card Card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Card"
    ADD CONSTRAINT "Card_pkey" PRIMARY KEY (id);


--
-- TOC entry 3148 (class 2606 OID 16418)
-- Name: CurveData CurveData_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CurveData"
    ADD CONSTRAINT "CurveData_pkey" PRIMARY KEY (id);


--
-- TOC entry 3152 (class 2606 OID 16431)
-- Name: Deck Deck_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Deck"
    ADD CONSTRAINT "Deck_pkey" PRIMARY KEY (id);


--
-- TOC entry 3150 (class 2606 OID 16426)
-- Name: PersonalData PersonalData_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PersonalData"
    ADD CONSTRAINT "PersonalData_pkey" PRIMARY KEY (id);


--
-- TOC entry 3157 (class 2606 OID 16458)
-- Name: Deck accountOwningDeck; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Deck"
    ADD CONSTRAINT "accountOwningDeck" FOREIGN KEY ("accountId") REFERENCES public."Account"(id) NOT VALID;


--
-- TOC entry 3156 (class 2606 OID 16465)
-- Name: PersonalData accountOwningPersonalData; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PersonalData"
    ADD CONSTRAINT "accountOwningPersonalData" FOREIGN KEY ("accountId") REFERENCES public."Account"(id) NOT VALID;


--
-- TOC entry 3158 (class 2606 OID 16452)
-- Name: Card containingDeck; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Card"
    ADD CONSTRAINT "containingDeck" FOREIGN KEY ("deckId") REFERENCES public."Deck"(id) NOT VALID;


--
-- TOC entry 3155 (class 2606 OID 16445)
-- Name: CurveData designatedAccount; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CurveData"
    ADD CONSTRAINT "designatedAccount" FOREIGN KEY ("accountId") REFERENCES public."Account"(id) NOT VALID;


-- Completed on 2022-06-28 10:22:45 EDT

--
-- PostgreSQL database dump complete
--

